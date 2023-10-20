package com.laser.ordermanage.order.repository;

import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.common.exception.ErrorCode;
import com.laser.ordermanage.customer.dto.response.GetOrderRes;
import com.laser.ordermanage.customer.dto.response.QGetOrderRes;
import com.laser.ordermanage.factory.dto.response.GetNewIssueNewOrderRes;
import com.laser.ordermanage.factory.dto.response.GetOrderReIssueRes;
import com.laser.ordermanage.factory.dto.response.QGetNewIssueNewOrderRes;
import com.laser.ordermanage.factory.dto.response.QGetOrderReIssueRes;
import com.laser.ordermanage.order.domain.type.Stage;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.criteria.JpaSubQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.laser.ordermanage.order.domain.QOrder.order;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GetOrderRes> findByCustomer(String userName, Pageable pageable, List<String> stageRequestList, List<String> manufacturingRequestList, String query) {
        List<GetOrderRes> getOrderResList = queryFactory
                .select(new QGetOrderRes(
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
                        eqName(query)
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
                        eqName(query)
                );

        return PageableExecutionUtils.getPage(getOrderResList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<GetOrderReIssueRes> findNewReIssueByFactory(String userName, Pageable pageable, Boolean hasQuotation, Boolean isUrgent) {
        List<GetOrderReIssueRes> getOrderReIssueResList = queryFactory
                .select(new QGetOrderReIssueRes(
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

        return PageableExecutionUtils.getPage(getOrderReIssueResList, pageable, countQuery::fetchOne);
    }

    @Override
    public Page<GetNewIssueNewOrderRes> findNewIssueNewByFactory(String userName, Pageable pageable, Boolean hasQuotation, Boolean isNewCustomer, Boolean isUrgent) {
        List<GetNewIssueNewOrderRes> getNewIssueNewOrderResList = queryFactory
                .select(new QGetNewIssueNewOrderRes(
                        order.id,
                        order.name,
                        order.customer.name,
                        order.customer.companyName,
                        order.quotation_id.isNotNull(),
                        order.customer.isNew,
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

    private BooleanExpression eqName(String name){
        return name == null ? null : order.name.contains(name);
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
}
