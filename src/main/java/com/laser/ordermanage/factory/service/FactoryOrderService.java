package com.laser.ordermanage.factory.service;

import com.laser.ordermanage.common.cloud.aws.S3Service;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.common.mail.MailService;
import com.laser.ordermanage.factory.dto.request.FactoryCreateOrUpdateOrderQuotationRequest;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateOrderIsUrgentRequest;
import com.laser.ordermanage.factory.dto.response.FactoryCreateOrUpdateOrderQuotationResponse;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.Quotation;
import com.laser.ordermanage.order.repository.QuotationRepository;
import com.laser.ordermanage.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class FactoryOrderService {

    private final QuotationRepository quotationRepository;

    private final OrderService orderService;
    private final MailService mailService;
    private final S3Service s3Service;

    @Transactional
    public Order updateOrderIsUrgent(Long orderId, FactoryUpdateOrderIsUrgentRequest request) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableUpdateIsUrgent()) {
            throw new CustomCommonException(ErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        order.updateIsUrgent(request.getIsUrgent());

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
        // 도면 파일 유무 확인
        if (file == null || file.isEmpty()) {
            throw new CustomCommonException(ErrorCode.MISSING_QUOTATION_FILE);
        }

        String fileName = file.getOriginalFilename();
        Long fileSize = file.getSize();

        // 견적서 파일 업로드
        String quotationFileUrl = uploadQuotationFile(file);

        Quotation quotation = Quotation.builder()
                .totalCost(request.getTotalCost())
                .fileName(fileName)
                .fileSize(fileSize)
                .fileUrl(quotationFileUrl)
                .deliveryDate(request.getDeliveryDate())
                .build();

        Quotation savedQuotation = quotationRepository.save(quotation);
        order.createQuotation(savedQuotation);

        return FactoryCreateOrUpdateOrderQuotationResponse.builder()
                .id(savedQuotation.getId())
                .fileName(savedQuotation.getFileName())
                .fileUrl(savedQuotation.getFileUrl())
                .build();
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

        // 도면 파일 유무 확인
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Long fileSize = file.getSize();

            // 견적서 파일 업로드
            String quotationFileUrl = uploadQuotationFile(file);

            quotation.updateFile(fileName, fileSize, quotationFileUrl);
        }

        quotation.updateQuotationProperties(request);

        return FactoryCreateOrUpdateOrderQuotationResponse.builder()
                .id(quotation.getId())
                .fileName(quotation.getFileName())
                .fileUrl(quotation.getFileUrl())
                .build();
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
            throw new CustomCommonException(ErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        if (!order.hasPurchaseOrder()) {
            throw new CustomCommonException(ErrorCode.MISSING_PURCHASE_ORDER);
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
    public Order changeStageToShipping(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableChangeStageToShipping()) {
            throw new CustomCommonException(ErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        order.changeStageToShipping();

        return order;
    }

    @Transactional(readOnly = true)
    public void sendEmailForChangeStageToShipping(Order order) {
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

    public String uploadQuotationFile(MultipartFile multipartFile) {
        return s3Service.upload("quotation", multipartFile);
    }
}
