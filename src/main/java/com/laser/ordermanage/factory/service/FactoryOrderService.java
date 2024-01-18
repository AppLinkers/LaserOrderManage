package com.laser.ordermanage.factory.service;

import com.laser.ordermanage.common.cloud.aws.S3Service;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.util.FileUtil;
import com.laser.ordermanage.customer.domain.Customer;
import com.laser.ordermanage.factory.dto.request.FactoryCreateOrUpdateOrderQuotationRequest;
import com.laser.ordermanage.factory.dto.request.FactoryCreateOrderAcquirerRequest;
import com.laser.ordermanage.factory.dto.request.FactoryUpdateOrderIsUrgentRequest;
import com.laser.ordermanage.factory.dto.response.FactoryCreateOrUpdateOrderQuotationResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetOrderCustomerResponse;
import com.laser.ordermanage.factory.dto.response.FactoryGetPurchaseOrderFileResponse;
import com.laser.ordermanage.order.domain.Acquirer;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.PurchaseOrder;
import com.laser.ordermanage.order.domain.Quotation;
import com.laser.ordermanage.order.domain.type.QuotationFileType;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.repository.AcquirerRepository;
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
    private final AcquirerRepository acquirerRepository;

    private final OrderService orderService;
    private final S3Service s3Service;

    @Transactional
    public void updateOrderIsUrgent(Long orderId, FactoryUpdateOrderIsUrgentRequest request) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableUpdateIsUrgent()) {
            throw new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        order.updateIsUrgent(request.isUrgent());
    }

    @Transactional
    public FactoryCreateOrUpdateOrderQuotationResponse createOrderQuotation(Long orderId, MultipartFile file, FactoryCreateOrUpdateOrderQuotationRequest request) {
        Order order = orderService.getOrderById(orderId);
        // 견적서 파일 유무 확인
        if (file == null || file.isEmpty()) {
            throw new CustomCommonException(OrderErrorCode.REQUIRED_QUOTATION_FILE);
        }

        String fileName = file.getOriginalFilename();
        Long fileSize = file.getSize();
        String fileType = FileUtil.getExtension(file);

        // 견적서 파일 업로드
        String quotationFileUrl = uploadQuotationFile(file);

        Quotation quotation = Quotation.builder()
                .totalCost(request.totalCost())
                .fileName(fileName)
                .fileSize(fileSize)
                .fileType(fileType)
                .fileUrl(quotationFileUrl)
                .deliveryDate(request.deliveryDate())
                .build();

        Quotation savedQuotation = quotationRepository.save(quotation);
        order.createQuotation(savedQuotation);

        return FactoryCreateOrUpdateOrderQuotationResponse.from(savedQuotation);
    }

    @Transactional
    public FactoryCreateOrUpdateOrderQuotationResponse updateOrderQuotation(Long orderId, MultipartFile file, FactoryCreateOrUpdateOrderQuotationRequest request) {
        Order order = orderService.getOrderById(orderId);
        Quotation quotation = order.getQuotation();

        // 견적서 파일 유무 확인
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            Long fileSize = file.getSize();
            String fileType = FileUtil.getExtension(file);

            // 견적서 파일 업로드
            String quotationFileUrl = uploadQuotationFile(file);

            quotation.updateFile(fileName, fileSize, fileType, quotationFileUrl);
        }

        quotation.updateProperties(request);

        return FactoryCreateOrUpdateOrderQuotationResponse.from(quotation);
    }

    @Transactional
    public void approvePurchaseOrder(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableApprovePurchaseOrder()) {
            throw new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        if (!order.hasPurchaseOrder()) {
            throw new CustomCommonException(OrderErrorCode.NOT_FOUND_PURCHASE_ORDER);
        }

        order.approvePurchaseOrder();
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

    @Transactional
    public void createOrderAcquirer(Long orderId, FactoryCreateOrderAcquirerRequest request, MultipartFile file) {
        Order order = orderService.getOrderById(orderId);

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
    public void changeStageToCompleted(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        order.changeStageToCompleted();

        Customer customer = order.getCustomer();
        if (customer.isNewCustomer()) {
            customer.disableNewCustomer();
        }
    }

    @Transactional(readOnly = true)
    public FactoryGetPurchaseOrderFileResponse getOrderPurchaseOrderFile(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (!order.hasPurchaseOrder()) {
            throw new CustomCommonException(OrderErrorCode.NOT_FOUND_PURCHASE_ORDER);
        }

        PurchaseOrder purchaseOrder = order.getPurchaseOrder();

        return FactoryGetPurchaseOrderFileResponse.builder()
                .id(purchaseOrder.getId())
                .fileName(purchaseOrder.getFileName())
                .fileUrl(purchaseOrder.getFileUrl())
                .build();
    }

    @Transactional(readOnly = true)
    public FactoryGetOrderCustomerResponse getOrderCustomer(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        Customer customer = order.getCustomer();

        return FactoryGetOrderCustomerResponse.builder()
                .orderId(order.getId())
                .orderName(order.getName())
                .customer(customer)
                .build();
    }

    private String uploadQuotationFile(MultipartFile multipartFile) {
        return s3Service.upload("quotation", multipartFile);
    }

    private String uploadAcquirerSignatureFile(MultipartFile multipartFile) {
        return s3Service.upload("acquirer-signature", multipartFile);
    }
}
