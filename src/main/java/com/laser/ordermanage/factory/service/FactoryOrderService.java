package com.laser.ordermanage.factory.service;

import com.laser.ordermanage.common.component.FileComponent;
import com.laser.ordermanage.common.entity.embedded.FileEntity;
import com.laser.ordermanage.common.exception.CustomCommonException;
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
import com.laser.ordermanage.order.domain.type.SignatureFileType;
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

    private final FileComponent fileComponent;

    private final QuotationRepository quotationRepository;
    private final AcquirerRepository acquirerRepository;

    private final OrderService orderService;

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

        if (order.getCreatedAt().toLocalDate().isAfter(request.deliveryDate())) {
            throw new CustomCommonException(OrderErrorCode.INVALID_QUOTATION_DELIVERY_DATE);
        }

        // 견적서 파일 유무 확인
        if (file == null || file.isEmpty()) {
            throw new CustomCommonException(OrderErrorCode.REQUIRED_QUOTATION_FILE);
        }

        FileEntity<QuotationFileType> quotationFile = fileComponent.uploadFile(file, QuotationFileType::ofExtension);

        Quotation quotation = request.toEntity(quotationFile);

        Quotation createdQuotation = quotationRepository.save(quotation);
        order.createQuotation(createdQuotation);

        return FactoryCreateOrUpdateOrderQuotationResponse.from(createdQuotation);
    }

    @Transactional
    public FactoryCreateOrUpdateOrderQuotationResponse updateOrderQuotation(Long orderId, MultipartFile file, FactoryCreateOrUpdateOrderQuotationRequest request) {
        Order order = orderService.getOrderById(orderId);
        Quotation quotation = order.getQuotation();

        if (order.getCreatedAt().toLocalDate().isAfter(request.deliveryDate())) {
            throw new CustomCommonException(OrderErrorCode.INVALID_QUOTATION_DELIVERY_DATE);
        }

        // 견적서 파일 유무 확인
        if (file != null && !file.isEmpty()) {
            FileEntity<QuotationFileType> quotationFile = fileComponent.uploadFile(file, QuotationFileType::ofExtension);

            quotation.updateFile(quotationFile);
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
    public void changeStageToProductionCompleted(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (!order.enableChangeStageToProductionCompleted()) {
            throw new CustomCommonException(OrderErrorCode.INVALID_ORDER_STAGE, order.getStage().getValue());
        }

        order.changeStageToProductionCompleted();
    }

    @Transactional
    public void createOrderAcquirer(Long orderId, FactoryCreateOrderAcquirerRequest request, MultipartFile file) {
        Order order = orderService.getOrderById(orderId);

        // 인수자 서명 파일 업로드
        FileEntity<SignatureFileType> signatureFile = fileComponent.uploadFile(file, SignatureFileType::ofExtension);

        Acquirer acquirer = request.toEntity(signatureFile);

        Acquirer createdAcquirer = acquirerRepository.save(acquirer);
        order.createAcquirer(createdAcquirer);
    }

    @Transactional
    public void changeStageToCompleted(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        order.changeStageToCompleted();

        Customer customer = order.getCustomer();
        customer.disableNewCustomer();
    }

    @Transactional(readOnly = true)
    public FactoryGetOrderCustomerResponse getOrderCustomer(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        Customer customer = order.getCustomer();

        return FactoryGetOrderCustomerResponse.fromEntity(order, customer);
    }

    @Transactional(readOnly = true)
    public FactoryGetPurchaseOrderFileResponse getOrderPurchaseOrderFile(Long orderId) {
        Order order = orderService.getOrderById(orderId);

        if (!order.hasPurchaseOrder()) {
            throw new CustomCommonException(OrderErrorCode.NOT_FOUND_PURCHASE_ORDER);
        }

        PurchaseOrder purchaseOrder = order.getPurchaseOrder();

        return FactoryGetPurchaseOrderFileResponse.fromEntity(purchaseOrder);
    }

}
