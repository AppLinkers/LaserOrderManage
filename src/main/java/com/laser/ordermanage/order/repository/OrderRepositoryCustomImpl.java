package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.customer.dto.response.GetCustomerOrderRes;
import com.laser.ordermanage.customer.dto.response.QGetCustomerOrderRes;
import com.laser.ordermanage.factory.dto.response.*;
import com.laser.ordermanage.order.domain.type.Stage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static com.laser.ordermanage.order.domain.QOrder.order;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GetCustomerOrderRes> findByCustomer(String userName, Pageable pageable, List<String> stageRequestList, List<String> manufacturingRequestList, String query) {
        List<GetCustomerOrderRes> getCustomerOrderResList = queryFactory
                .select(new QGetCustomerOrderRes(
                        order.id,
                        order.name,
                        order.imgUrl,
                        order.stage,
                        order.isUrgent,
                        order.manufacturing,
                        order.createdAt,
                        order.quotation_delivery_date,
                        order.quotation_total_cost,
                        order.request
                ))
                .from(order)
                .where(
                        order.customer.user.email.eq(userName),
                        eqStage(stageRequestList),
                        eqManufacturing(manufacturingRequestList),
                        query == null ? null : order.name.contains(query)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(
                        order.customer.user.email.eq(userName),
                        eqStage(stageRequestList),
                        eqManufacturing(manufacturingRequestList),
                        query == null ? null : order.name.contains(query)
                );

        return PageableExecutionUtils.getPage(getCustomerOrderResList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<GetReIssueNewOrderRes> findIsNewAndIsReIssueByFactory(Pageable pageable, Boolean hasQuotation, Boolean isUrgent) {
        List<GetReIssueNewOrderRes> getReIssueNewOrderResList = queryFactory
                .select(new QGetReIssueNewOrderRes(
                        order.id,
                        order.name,
                        order.customer.name,
                        order.customer.companyName,
                        order.quotation_id.isNotNull(),
                        order.imgUrl,
                        order.isUrgent,
                        order.manufacturing,
                        order.createdAt,
                        order.quotation_delivery_date,
                        order.quotation_total_cost,
                        order.request
                ))
                .from(order)
                .where(
                        order.stage.eq(Stage.NEW),
                        order.isNewIssue.eq(Boolean.FALSE),
                        eqHasQuotation(hasQuotation),
                        eqIsUrgent(isUrgent)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(
                        order.stage.eq(Stage.NEW),
                        order.isNewIssue.eq(Boolean.FALSE),
                        eqHasQuotation(hasQuotation),
                        eqIsUrgent(isUrgent)
                );

        return PageableExecutionUtils.getPage(getReIssueNewOrderResList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<GetNewIssueNewOrderRes> findIsNewAndIsNewIssueByFactory(Pageable pageable, Boolean hasQuotation, Boolean isNewCustomer, Boolean isUrgent) {
        List<GetNewIssueNewOrderRes> getNewIssueNewOrderResList = queryFactory
                .select(new QGetNewIssueNewOrderRes(
                        order.id,
                        order.name,
                        order.customer.name,
                        order.customer.companyName,
                        order.customer.isNew,
                        order.quotation_id.isNotNull(),
                        order.imgUrl,
                        order.isUrgent,
                        order.manufacturing,
                        order.createdAt,
                        order.quotation_delivery_date,
                        order.quotation_total_cost,
                        order.request
                ))
                .from(order)
                .where(
                        order.stage.eq(Stage.NEW),
                        order.isNewIssue.eq(Boolean.TRUE),
                        eqHasQuotation(hasQuotation),
                        eqIsNewCustomer(isNewCustomer),
                        eqIsUrgent(isUrgent)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(
                        order.stage.eq(Stage.NEW),
                        order.isNewIssue.eq(Boolean.TRUE),
                        eqHasQuotation(hasQuotation),
                        eqIsNewCustomer(isNewCustomer),
                        eqIsUrgent(isUrgent)
                );

        return PageableExecutionUtils.getPage(getNewIssueNewOrderResList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<GetFactoryOrderRes> findByFactory(Pageable pageable, Boolean isCompleted, Boolean isUrgent, String dateCriterion, LocalDate startDate, LocalDate endDate, String query) {
        List<GetFactoryOrderRes> getFactoryOrderResList = queryFactory
                .select(new QGetFactoryOrderRes(
                        order.id,
                        order.name,
                        order.customer.name,
                        order.customer.companyName,
                        order.imgUrl,
                        order.stage,
                        order.isUrgent,
                        order.manufacturing,
                        order.createdAt,
                        order.quotation_delivery_date,
                        order.quotation_total_cost,
                        order.request
                ))
                .from(order)
                .where(
                        eqIsCompleted(isCompleted),
                        eqIsUrgent(isUrgent),
                        searchDateFilter(dateCriterion, startDate, endDate),
                        query == null ? null : order.name.contains(query)
                                .or(order.customer.name.contains(query))
                                .or(order.customer.companyName.contains(query))
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(order.count())
                .from(order)
                .where(
                        eqIsCompleted(isCompleted),
                        eqIsUrgent(isUrgent),
                        searchDateFilter(dateCriterion, startDate, endDate),
                        query == null ? null : order.name.contains(query)
                                .or(order.customer.name.contains(query))
                                .or(order.customer.companyName.contains(query))
                );

        return PageableExecutionUtils.getPage(getFactoryOrderResList, pageable, countQuery::fetchOne);
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
                manufacturing -> {
                    switch (manufacturing) {
                        case "laser-cutting" -> booleanBuilder.or(order.manufacturing.isLaserCutting.eq(Boolean.TRUE));
                        case "bending" -> booleanBuilder.or(order.manufacturing.isBending.eq(Boolean.TRUE));
                        case "welding-fabrication" -> booleanBuilder.or(order.manufacturing.isWeldingFabrication.eq(Boolean.TRUE));
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
            return booleanBuilder.and(order.quotation_id.isNotNull());
        } else {
            return booleanBuilder.and(order.quotation_id.isNull());
        }
    }

    private BooleanBuilder eqIsNewCustomer(Boolean isNewCustomer) {
        if (isNewCustomer == null) {
            return null;
        }

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if (isNewCustomer) {
            return booleanBuilder.and(order.customer.isNew);
        } else {
            return booleanBuilder.and(order.customer.isNew.not());
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
            BooleanExpression isGoeStartDate = order.quotation_delivery_date.goe(startDate);
            BooleanExpression isLoeEndDate = order.quotation_delivery_date.loe(endDate);

            return booleanBuilder.and(isGoeStartDate).and(isLoeEndDate);
        }
        return null;
    }
}
