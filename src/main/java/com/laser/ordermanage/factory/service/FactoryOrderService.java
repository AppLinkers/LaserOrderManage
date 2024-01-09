package com.laser.ordermanage.factory.service;

import com.laser.ordermanage.common.cloud.aws.S3Service;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.mail.MailService;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.factory.dto.request.FactoryCreateOrUpdateOrderQuotationRequest;
import com.laser.ordermanage.factory.dto.request.FactoryCreateOrderAcquirerRequest;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateOrderIsUrgentRequest;
import com.laser.ordermanage.factory.dto.response.FactoryCreateOrUpdateOrderQuotationResponse;
import com.laser.ordermanage.order.domain.Acquirer;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.Quotation;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.repository.AcquirerRepository;
import com.laser.ordermanage.order.repository.QuotationRepository;
import com.laser.ordermanage.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Service
public class FactoryOrderService {

    private final QuotationRepository quotationRepository;
    private final AcquirerRepository acquirerRepository;

    private final OrderService orderService;
    private final MailService mailService;
    private final S3Service s3Service;

    @Transactional
    public Order updateOrderIsUrgent(Long orderId, FactoryUpdateOrderIsUrgentRequest request) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableUpdateIsUrgent()) {
            throw new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        order.updateIsUrgent(request.isUrgent());

        return order;
    }

    @Transactional(readOnly = true)
    public void sendMailForUpdateOrderIsUrgent(Order order) {
        String toEmail = order.getCustomer().getUser().getEmail();

        StringBuilder sbTitle = new StringBuilder();
        StringBuilder sbContent = new StringBuilder();

        if (order.getIsUrgent()) {
            sbTitle.append("[거래 긴급 설정] ")
                    .append(order.getName())
                    .append(" 거래의 긴급이 설정 되었습니다.");

            sbContent.append("고객님, ")
                    .append(order.getName())
                    .append(" 거래의 긴급이 설정 되었습니다.");
        } else {
            sbTitle.append("[거래 긴급 설정 해제] ")
                    .append(order.getName())
                    .append(" 거래의 긴급 설정이 해제 되었습니다.");

            sbContent.append("고객님, ")
                    .append(order.getName())
                    .append(" 거래의 긴급 설정이 해제 되었습니다.");
        }

        String title = sbTitle.toString();
        String content = sbContent.toString();

        mailService.sendEmail(toEmail, title, content);
    }

    @Transactional
    public FactoryCreateOrUpdateOrderQuotationResponse createOrderQuotation(Order order, MultipartFile file, FactoryCreateOrUpdateOrderQuotationRequest request) {
        // 견적서 파일 유무 확인
        if (file == null || file.isEmpty()) {
            throw new CustomCommonException(OrderErrorCode.REQUIRED_QUOTATION_FILE);
        }

        String fileName = file.getOriginalFilename();
        Long fileSize = file.getSize();

        // 견적서 파일 업로드
        String quotationFileUrl = uploadQuotationFile(file);

        Quotation quotation = Quotation.builder()
                .totalCost(request.totalCost())
                .fileName(fileName)
                .fileSize(fileSize)
                .fileUrl(quotationFileUrl)
                .deliveryDate(request.deliveryDate())
                .build();

        Quotation savedQuotation = quotationRepository.save(quotation);
        order.createQuotation(savedQuotation);

        return FactoryCreateOrUpdateOrderQuotationResponse.from(savedQuotation);
    }

    @Transactional(readOnly = true)
    public void sendMailForCreateOrderQuotation(Order order) {
        String toEmail = order.getCustomer().getUser().getEmail();

        StringBuilder sbTitle = new StringBuilder();
        sbTitle.append("[거래 견적서 작성] 고객님, ")
                .append(order.getName())
                .append(" 거래의 견적서가 작성되었습니다.");
        String title = sbTitle.toString();

        StringBuilder sbContent = new StringBuilder();
        sbContent.append("고객님, ")
                .append(order.getName())
                .append(" 거래의 견적서가 작성되었습니다.");
        String content = sbContent.toString();

        mailService.sendEmail(toEmail, title, content);
    }

    @Transactional
    public FactoryCreateOrUpdateOrderQuotationResponse updateOrderQuotation(Order order, MultipartFile file, FactoryCreateOrUpdateOrderQuotationRequest request) {
        Quotation quotation = order.getQuotation();

        // 견적서 파일 유무 확인
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Long fileSize = file.getSize();

            // 견적서 파일 업로드
            String quotationFileUrl = uploadQuotationFile(file);

            quotation.updateFile(fileName, fileSize, quotationFileUrl);
        }

        quotation.updateProperties(request);

        return FactoryCreateOrUpdateOrderQuotationResponse.from(quotation);
    }

    @Transactional(readOnly = true)
    public void sendMailForUpdateOrderQuotation(Order order) {
        String toEmail = order.getCustomer().getUser().getEmail();

        StringBuilder sbTitle = new StringBuilder();
        sbTitle.append("[거래 견적서 작성] 고객님, ")
                .append(order.getName())
                .append(" 거래의 견적서가 수정되었습니다.");
        String title = sbTitle.toString();

        StringBuilder sbContent = new StringBuilder();
        sbContent.append("고객님, ")
                .append(order.getName())
                .append(" 거래의 견적서가 수정되었습니다.");
        String content = sbContent.toString();

        mailService.sendEmail(toEmail, title, content);
    }

    @Transactional
    public Order approvePurchaseOrder(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableApprovePurchaseOrder()) {
            throw new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        if (!order.hasPurchaseOrder()) {
            throw new CustomCommonException(OrderErrorCode.NOT_FOUND_PURCHASE_ORDER);
        }

        order.approvePurchaseOrder();

        return order;
    }

    @Transactional(readOnly = true)
    public void sendEmailForApprovePurchaseOrder(Order order) {
        String toEmail = order.getCustomer().getUser().getEmail();

        StringBuilder sbTitle = new StringBuilder();
        sbTitle.append("[거래 발주서 승인] 고객님, ")
                .append(order.getName())
                .append(" 거래의 발주서가 승인되었습니다.");
        String title = sbTitle.toString();

        StringBuilder sbContent = new StringBuilder();
        sbContent.append("고객님, ")
                .append(order.getName())
                .append(" 거래의 발주서가 승인되었습니다.");
        String content = sbContent.toString();

        mailService.sendEmail(toEmail, title, content);
    }

    @Transactional
    public Order changeStageToProductionCompleted(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableChangeStageToProductionCompleted()) {
            throw new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        order.changeStageToProductionCompleted();

        return order;
    }

    @Transactional(readOnly = true)
    public void sendEmailForChangeStageToProductionCompleted(Order order) {
        String toEmail = order.getCustomer().getUser().getEmail();

        StringBuilder sbTitle = new StringBuilder();
        sbTitle.append("[거래 제작 완료] 고객님, ")
                .append(order.getName())
                .append(" 거래의 제작이 완료되었습니다.");
        String title = sbTitle.toString();

        StringBuilder sbContent = new StringBuilder();
        sbContent.append("고객님, ")
                .append(order.getName())
                .append(" 거래의 제작이 완료되었습니다.");
        String content = sbContent.toString();

        mailService.sendEmail(toEmail, title, content);
    }

    @Transactional(readOnly = true)
    public void sendEmailForAcquirer(Long orderId, String baseUrl) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableChangeStageToCompleted()) {
            throw new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        String acquireSignatureUrl = UriComponentsBuilder
                .fromHttpUrl(baseUrl)
                .path("/{order-id}")
                .buildAndExpand(order.getId())
                .toUriString();

        StringBuilder sbTitle = new StringBuilder();
        sbTitle.append("[거래 품목 확인 및 인수 서명 요청] ")
                .append(order.getCustomer().getName())
                .append(" - ")
                .append(order.getName())
                .append(" 거래의 품목을 확인 후 인수자의 서명을 받아주세요.");
        String title = sbTitle.toString();

        StringBuilder sbContent = new StringBuilder();
        sbContent.append("거래 확인 및 인수자 서명 링크 : ")
                .append(acquireSignatureUrl);
        String content = sbContent.toString();

        mailService.sendEmailToFactory(title, content);
    }

    @Transactional
    public void createOrderAcquirer(Order order, FactoryCreateOrderAcquirerRequest request, MultipartFile file) {

        String fileName = file.getOriginalFilename();
        Long fileSize = file.getSize();

        // 인수자 서명 파일 업로드
        String acquirerSignatureFileUrl = uploadAcquirerSignatureFile(file);

        Acquirer acquirer = Acquirer.builder()
                .name(request.name())
                .phone(request.phone())
                .signatureFileName(fileName)
                .signatureFileSize(fileSize)
                .signatureFileUrl(acquirerSignatureFileUrl)
                .build();

        Acquirer savedAcquirer = acquirerRepository.save(acquirer);
        order.createAcquirer(savedAcquirer);
    }

    @Transactional
    public void changeStageToCompleted(Order order) {
        order.changeStageToCompleted();

        Customer customer = order.getCustomer();
        if (customer.isNewCustomer()) {
            customer.disableNewCustomer();
        }
    }

    @Transactional(readOnly = true)
    public void sendEmailForChangeStageToCompleted(Order order) {
        String toEmail = order.getCustomer().getUser().getEmail();

        StringBuilder sbTitle = new StringBuilder();
        sbTitle.append("[거래 완료] 고객님, ")
                .append(order.getName())
                .append(" 거래가 완료되었습니다.");
        String title = sbTitle.toString();

        StringBuilder sbContent = new StringBuilder();
        sbContent.append("고객님, ")
                .append(order.getName())
                .append(" 거래가 완료되었습니다.\n");

        if (order.hasAcquirer()) {
            sbContent.append("인수자 정보\n이름 : ")
                    .append(order.getAcquirer().getName())
                    .append("\n핸드폰 번호 : ")
                    .append(order.getAcquirer().getPhone());
        }

        String content = sbContent.toString();

        mailService.sendEmail(toEmail, title, content);
    }

    private String uploadQuotationFile(MultipartFile multipartFile) {
        return s3Service.upload("quotation", multipartFile);
    }

    private String uploadAcquirerSignatureFile(MultipartFile multipartFile) {
        return s3Service.upload("acquirer-signature", multipartFile);
    }
}
