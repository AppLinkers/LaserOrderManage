package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.customer.dto.response.*;
import com.laser.ordermanage.factory.dto.response.*;
import com.laser.ordermanage.order.domain.type.Stage;
import com.laser.ordermanage.order.dto.response.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.laser.ordermanage.customer.domain.QCustomer.customer;
import static com.laser.ordermanage.customer.domain.QDeliveryAddress.deliveryAddress;
import static com.laser.ordermanage.order.domain.QDrawing.drawing;
import static com.laser.ordermanage.order.domain.QOrder.order;
import static com.laser.ordermanage.order.domain.QOrderManufacturing.orderManufacturing;
import static com.laser.ordermanage.order.domain.QOrderPostProcessing.orderPostProcessing;
import static com.laser.ordermanage.order.domain.QPurchaseOrder.purchaseOrder;
import static com.laser.ordermanage.order.domain.QQuotation.quotation;
import static com.laser.ordermanage.user.domain.QUserEntity.userEntity;
import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.list;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CustomerGetOrderHistoryResponse> findByCustomer(String userName, Pageable pageable, List<String> stageRequestList, List<String> manufacturingRequestList, String query) {
        List<CustomerGetOrderHistoryResponse> customerGetOrderHistoryResponseList = queryFactory
                .select(new QCustomerGetOrderHistoryResponse(
                        order.id,
                        order.name,
                        order.imgUrl,
                        order.stage,
                        order.isUrgent,
                        order.manufacturing,
                        order.createdAt,
                        quotation.deliveryDate,
                        quotation.totalCost,
                        order.request
                ))
                .from(order)
                .join(order.customer, customer)
                .join(customer.user, userEntity)
                .join(order.manufacturing, orderManufacturing)
                .leftJoin(order.quotation, quotation)
                .where(
                        userEntity.email.eq(userName),
                        eqStage(stageRequestList),
                        eqManufacturing(manufacturingRequestList),
                        query == null ? null : order.name.contains(query)
                )
                .orderBy(order.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .join(order.customer, customer)
                .join(customer.user, userEntity)
                .join(order.manufacturing, orderManufacturing)
                .where(
                        userEntity.email.eq(userName),
                        eqStage(stageRequestList),
                        eqManufacturing(manufacturingRequestList),
                        query == null ? null : order.name.contains(query)
                );

        return PageableExecutionUtils.getPage(customerGetOrderHistoryResponseList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> findIsNewAndIsReIssueByFactory(Pageable pageable, Boolean hasQuotation, Boolean isUrgent) {
        List<FactoryGetOrderIsNewAndIsReIssueHistoryResponse> factoryGetOrderIsNewAndIsReIssueHistoryResponseList = queryFactory
                .select(new QFactoryGetOrderIsNewAndIsReIssueHistoryResponse(
                        order.id,
                        order.name,
                        customer.name,
                        customer.companyName,
                        quotation.isNotNull(),
                        order.imgUrl,
                        order.isUrgent,
                        order.manufacturing,
                        order.createdAt,
                        quotation.deliveryDate,
                        quotation.totalCost,
                        order.request
                ))
                .from(order)
                .join(order.customer, customer)
                .leftJoin(order.quotation, quotation)
                .where(
                        order.stage.eq(Stage.NEW),
                        order.isNewIssue.eq(Boolean.FALSE),
                        eqHasQuotation(hasQuotation),
                        eqIsUrgent(isUrgent)
                )
                .orderBy(order.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .leftJoin(order.quotation, quotation)
                .where(
                        order.stage.eq(Stage.NEW),
                        order.isNewIssue.eq(Boolean.FALSE),
                        eqHasQuotation(hasQuotation),
                        eqIsUrgent(isUrgent)
                );

        return PageableExecutionUtils.getPage(factoryGetOrderIsNewAndIsReIssueHistoryResponseList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> findIsNewAndIsNewIssueByFactory(Pageable pageable, Boolean hasQuotation, Boolean isNewCustomer, Boolean isUrgent) {
        List<FactoryGetOrderIsNewAndIsNewIssueHistoryResponse> factoryGetOrderIsNewAndIsNewIssueHistoryResponseList = queryFactory
                .select(new QFactoryGetOrderIsNewAndIsNewIssueHistoryResponse(
                        order.id,
                        order.name,
                        customer.name,
                        customer.companyName,
                        customer.isNew,
                        quotation.isNotNull(),
                        order.imgUrl,
                        order.isUrgent,
                        order.manufacturing,
                        order.createdAt,
                        quotation.deliveryDate,
                        quotation.totalCost,
                        order.request
                ))
                .from(order)
                .join(order.customer, customer)
                .leftJoin(order.quotation, quotation)
                .where(
                        order.stage.eq(Stage.NEW),
                        order.isNewIssue.eq(Boolean.TRUE),
                        eqHasQuotation(hasQuotation),
                        eqIsNewCustomer(isNewCustomer),
                        eqIsUrgent(isUrgent)
                )
                .orderBy(order.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .leftJoin(order.quotation, quotation)
                .where(
                        order.stage.eq(Stage.NEW),
                        order.isNewIssue.eq(Boolean.TRUE),
                        eqHasQuotation(hasQuotation),
                        eqIsNewCustomer(isNewCustomer),
                        eqIsUrgent(isUrgent)
                );

        return PageableExecutionUtils.getPage(factoryGetOrderIsNewAndIsNewIssueHistoryResponseList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<FactoryGetOrderHistoryResponse> findByFactory(Pageable pageable, Boolean isCompleted, Boolean isUrgent, String dateCriterion, LocalDate startDate, LocalDate endDate, String query) {
        List<FactoryGetOrderHistoryResponse> factoryGetOrderHistoryResponseList = queryFactory
                .select(new QFactoryGetOrderHistoryResponse(
                        order.id,
                        order.name,
                        customer.name,
                        customer.companyName,
                        order.imgUrl,
                        order.stage,
                        order.isUrgent,
                        order.manufacturing,
                        order.createdAt,
                        quotation.deliveryDate,
                        quotation.totalCost,
                        order.request
                ))
                .from(order)
                .join(order.customer, customer)
                .leftJoin(order.quotation, quotation)
                .where(
                        eqIsCompleted(isCompleted),
                        eqIsUrgent(isUrgent),
                        searchDateFilter(dateCriterion, startDate, endDate),
                        query == null ? null : order.name.contains(query)
                                .or(customer.name.contains(query))
                                .or(customer.companyName.contains(query))
                )
                .orderBy(order.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .join(order.customer, customer)
                .leftJoin(order.quotation, quotation)
                .where(
                        eqIsCompleted(isCompleted),
                        eqIsUrgent(isUrgent),
                        searchDateFilter(dateCriterion, startDate, endDate),
                        query == null ? null : order.name.contains(query)
                                .or(customer.name.contains(query))
                                .or(customer.companyName.contains(query))
                );

        return PageableExecutionUtils.getPage(factoryGetOrderHistoryResponseList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<CustomerGetOrderIsCompletedHistoryResponse> findIsCompletedByCustomer(String userName, Pageable pageable, String query) {
        List<CustomerGetOrderIsCompletedHistoryResponse> customerGetOrderIsCompletedHistoryResponseList = queryFactory
                .select(new QCustomerGetOrderIsCompletedHistoryResponse(
                        order.id,
                        order.name,
                        order.imgUrl,
                        order.createdAt
                ))
                .from(order)
                .join(order.customer, customer)
                .join(customer.user, userEntity)
                .where(
                        order.stage.eq(Stage.COMPLETED),
                        userEntity.email.eq(userName),
                        query == null ? null : order.name.contains(query)
                )
                .orderBy(order.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .join(order.customer, customer)
                .join(customer.user, userEntity)
                .where(
                        order.stage.eq(Stage.COMPLETED),
                        userEntity.email.eq(userName),
                        query == null ? null : order.name.contains(query)
                );

        return PageableExecutionUtils.getPage(customerGetOrderIsCompletedHistoryResponseList, pageable, countQuery::fetchOne);
    }

    @Override
    public CustomerGetOrderCreateInformationResponse findCreateInformationByCustomerAndOrder(String userName, Long orderId) {
        List<CustomerGetOrderCreateInformationResponse> customerGetOrderCreateInformationResponseList = queryFactory
                .selectFrom(order)
                .join(order.customer, customer)
                .join(customer.user, userEntity)
                .join(order.manufacturing, orderManufacturing)
                .join(order.postProcessing, orderPostProcessing)
                .join(drawing).on(order.id.eq(drawing.order.id))
                .join(order.deliveryAddress, deliveryAddress)
                .where(
                        userEntity.email.eq(userName),
                        order.id.eq(orderId)
                )
                .transform(
                        groupBy(order.id).list(
                                new QCustomerGetOrderCreateInformationResponse(
                                        order.id,
                                        order.name,
                                        orderManufacturing,
                                        orderPostProcessing,
                                        list(
                                                new QGetDrawingResponse(
                                                        drawing.id,
                                                        drawing.fileName,
                                                        drawing.fileSize,
                                                        drawing.fileType,
                                                        drawing.fileUrl,
                                                        drawing.thumbnailUrl,
                                                        drawing.count,
                                                        drawing.ingredient,
                                                        drawing.thickness
                                                )
                                        ),
                                        order.request,
                                        new QGetDeliveryAddressResponse(
                                                deliveryAddress.id,
                                                deliveryAddress.name,
                                                deliveryAddress.zipCode,
                                                deliveryAddress.address,
                                                deliveryAddress.detailAddress,
                                                deliveryAddress.receiver,
                                                deliveryAddress.phone1,
                                                deliveryAddress.phone2,
                                                deliveryAddress.isDefault,
                                                deliveryAddress.isDeleted
                                        )
                                )
                        )
                );

        return customerGetOrderCreateInformationResponseList.isEmpty() ? null : customerGetOrderCreateInformationResponseList.get(0);
    }

    @Override
    public GetOrderDetailResponse findDetailByOrder(Long orderId) {
        List<GetOrderDetailResponse> getOrderDetailResponseList = queryFactory
                .selectFrom(order)
                .join(order.customer, customer)
                .join(customer.user, userEntity)
                .join(drawing).on(order.id.eq(drawing.order.id))
                .join(order.deliveryAddress, deliveryAddress)
                .leftJoin(order.quotation, quotation)
                .leftJoin(order.purchaseOrder, purchaseOrder)
                .where(order.id.eq(orderId))
                .transform(
                        groupBy(order.id).list(
                               new QGetOrderDetailResponse(
                                       new QGetCustomerResponse(
                                              customer.id,
                                              customer.name,
                                              customer.companyName,
                                              userEntity.phone,
                                              userEntity.email
                                       ),
                                       new QGetOrderResponse(
                                              order.id,
                                              order.name,
                                              order.isUrgent,
                                              order.stage,
                                              order.manufacturing,
                                              order.postProcessing,
                                              list(
                                                      new QGetDrawingResponse(
                                                              drawing.id,
                                                              drawing.fileName,
                                                              drawing.fileSize,
                                                              drawing.fileType,
                                                              drawing.fileUrl,
                                                              drawing.thumbnailUrl,
                                                              drawing.count,
                                                              drawing.ingredient,
                                                              drawing.thickness
                                                      )
                                              ),
                                              order.request,
                                               new QGetDeliveryAddressResponse(
                                                       deliveryAddress.id,
                                                       deliveryAddress.name,
                                                       deliveryAddress.zipCode,
                                                       deliveryAddress.address,
                                                       deliveryAddress.detailAddress,
                                                       deliveryAddress.receiver,
                                                       deliveryAddress.phone1,
                                                       deliveryAddress.phone2,
                                                       deliveryAddress.isDefault,
                                                       deliveryAddress.isDeleted
                                               ),
                                               order.createdAt
                                       ),
                                       new QGetQuotationResponse(
                                               quotation.id,
                                               quotation.fileName,
                                               quotation.fileUrl,
                                               quotation.totalCost,
                                               quotation.deliveryDate,
                                               quotation.createdAt
                                       ).skipNulls(),
                                       new QGetPurchaseOrderResponse(
                                               purchaseOrder.id,
                                               purchaseOrder.inspectionPeriod,
                                               purchaseOrder.inspectionCondition,
                                               purchaseOrder.paymentDate,
                                               purchaseOrder.createdAt
                                       ).skipNulls()
                               )
                        )
                );

        return getOrderDetailResponseList.isEmpty() ? null : getOrderDetailResponseList.get(0);
    }

    @Override
    public Optional<String> findUserEmailById(Long orderId) {
        String userEmail = queryFactory
                .select(userEntity.email)
                .from(order)
                .join(order.customer, customer)
                .join(customer.user, userEntity)
                .where(order.id.eq(orderId))
                .fetchOne();

        return Optional.ofNullable(userEmail);
    }


    private BooleanBuilder eqStage(List<String> stageRequestList) {

        if (stageRequestList == null) {
            return null;
        }

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        stageRequestList.forEach(
                stage -> booleanBuilder.or(order.stage.eq(Stage.ofRequest(stage)))
        );

        return booleanBuilder;
    }

    private BooleanBuilder eqManufacturing(List<String> manufacturingRequestList) {

        if (manufacturingRequestList == null) {
            return null;
        }

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        manufacturingRequestList.forEach(
                manufacturingRequest -> {
                    switch (manufacturingRequest) {
                        case "laser-cutting" -> booleanBuilder.or(orderManufacturing.isLaserCutting.eq(Boolean.TRUE));
                        case "bending" -> booleanBuilder.or(orderManufacturing.isBending.eq(Boolean.TRUE));
                        case "welding-fabrication" -> booleanBuilder.or(orderManufacturing.isWeldingFabrication.eq(Boolean.TRUE));
                        default -> throw new CustomCommonException(ErrorCode.INVALID_PARAMETER, "manufacturing");
                    }
                }
        );

        return booleanBuilder;
    }

    private BooleanBuilder eqHasQuotation(Boolean hasQuotation) {
        if (hasQuotation == null) {
            return null;
        }

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (hasQuotation) {
            return booleanBuilder.and(quotation.isNotNull());
        } else {
            return booleanBuilder.and(quotation.isNull());
        }
    }

    private BooleanBuilder eqIsNewCustomer(Boolean isNewCustomer) {
        if (isNewCustomer == null) {
            return null;
        }

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (isNewCustomer) {
            return booleanBuilder.and(customer.isNew);
        } else {
            return booleanBuilder.and(customer.isNew.not());
        }
    }

    private BooleanBuilder eqIsUrgent(Boolean isUrgent) {
        if (isUrgent == null) {
            return null;
        }

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (isUrgent) {
            return booleanBuilder.and(order.isUrgent);
        } else {
            return booleanBuilder.and(order.isUrgent.not());
        }
    }

    private BooleanBuilder eqIsCompleted(Boolean isCompleted) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (isCompleted) {
            return booleanBuilder.and(order.stage.eq(Stage.COMPLETED));
        } else {
            return booleanBuilder.and(order.stage.ne(Stage.COMPLETED));
        }
    }

    private BooleanBuilder searchDateFilter(String dateCriterion, LocalDate startDate, LocalDate endDate) {
        if (dateCriterion == null || startDate == null || endDate == null) {
            return null;
        }

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (dateCriterion.equals("create")) {
            BooleanExpression isGoeStartDate = order.createdAt.goe(LocalDateTime.of(startDate, LocalTime.MIN));
            BooleanExpression isLoeEndDate = order.createdAt.loe(LocalDateTime.of(endDate, LocalTime.MAX).withNano(0));

            return booleanBuilder.and(isGoeStartDate).and(isLoeEndDate);
        } else if (dateCriterion.equals("delivery")) {
            BooleanExpression isGoeStartDate = quotation.deliveryDate.goe(startDate);
            BooleanExpression isLoeEndDate = quotation.deliveryDate.loe(endDate);

            return booleanBuilder.and(isGoeStartDate).and(isLoeEndDate);
        }
        return null;
    }
}
