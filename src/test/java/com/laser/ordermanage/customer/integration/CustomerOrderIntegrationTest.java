package com.laser.ordermanage.customer.integration;

import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.customer.dto.request.*;
import com.laser.ordermanage.customer.dto.response.CustomerCreateDrawingResponse;
import com.laser.ordermanage.customer.dto.response.CustomerCreateDrawingResponseBuilder;
import com.laser.ordermanage.customer.dto.response.CustomerCreateOrUpdateOrderPurchaseOrderResponse;
import com.laser.ordermanage.customer.dto.response.CustomerCreateOrUpdateOrderPurchaseOrderResponseBuilder;
import com.laser.ordermanage.customer.exception.CustomerErrorCode;
import com.laser.ordermanage.order.domain.type.Stage;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class CustomerOrderIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    /**
     * 고객 회원의 거래 생성 성공
     */
    @Test
    public void 고객_회원의_거래_생성_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrder(accessToken, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 고객 회원의 거래 생성 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 고객_회원의_거래_생성_실패_Header_Authorization_존재() throws Exception {
        // given
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrderWithOutAccessToken(request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 고객 회원의 거래 생성 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 고객_회원의_거래_생성_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrder(unauthorizedAccessToken, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 고객 회원의 거래 생성 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 고객_회원의_거래_생성_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrder(refreshToken, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 고객 회원의 거래 생성 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 고객_회원의_거래_생성_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrder(expiredAccessToken, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 고객 회원의 거래 생성 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 고객_회원의_거래_생성_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final CustomerCreateOrderRequest request = CustomerCreateOrderRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrder(invalidToken, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 배송지 수정 성공
     */
    @Test
    public void 거래_배송지_수정_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "3";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddress(accessToken, orderId, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 배송지 수정 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_배송지_수정_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "3";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddressWithOutAccessToken(orderId, request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래 배송지 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_배송지_수정_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "3";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddress(unauthorizedAccessToken, orderId, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래 배송지 수정 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_배송지_수정_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();
        final String orderId = "3";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddress(refreshToken, orderId, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 배송지 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_배송지_수정_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "3";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddress(expiredAccessToken, orderId, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 배송지 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_배송지_수정_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "3";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddress(invalidToken, orderId, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 배송지 수정 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    public void 거래_배송지_수정_실패_거래_접근_권한() throws Exception {
        // given
        final String accessTokenOfUser2 = jwtBuilder.accessJwtBuildOfUser2();
        final String orderId = "3";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress8();

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddress(accessTokenOfUser2, orderId, request);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래 배송지 수정 실패
     * - 실패 사유 : 배송지에 대한 접근 권한이 없음
     */
    @Test
    public void 거래_배송지_수정_실패_배송지_접근_권한() throws Exception {
        // given
        final String accessTokenOfUser2 = jwtBuilder.accessJwtBuildOfUser2();
        final String orderId = "8";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddress(accessTokenOfUser2, orderId, request);

        // then
        assertError(CustomerErrorCode.DENIED_ACCESS_TO_DELIVERY_ADDRESS, resultActions);
    }

    /**
     * 거래 배송지 수정 실패
     * - 실패 사유 : 거래 단계가 배송지 수정 가능 단계(견적 대기, 견적 승인, 제작 중)가 아님
     */
    @Test
    public void 거래_배송지_수정_실패_거래배송지수정_가능단계() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "1";
        final CustomerUpdateOrderDeliveryAddressRequest request = CustomerUpdateOrderDeliveryAddressRequestBuilder.buildOfDeliveryAddress2();

        // when
        final ResultActions resultActions = requestUpdateOrderDeliveryAddress(accessToken, orderId, request);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래 도면 항목 추가 성공
     */
    @Test
    public void 거래_도면_항목_추가_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "3";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.build();
        final CustomerCreateDrawingResponse expectedResponse = CustomerCreateDrawingResponseBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(accessToken, orderId, request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final CustomerCreateDrawingResponse actualResponse = objectMapper.readValue(responseString, CustomerCreateDrawingResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }


    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_도면_항목_추가_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "3";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrderDrawingWithOutAccessToken(orderId, request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_도면_항목_추가_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "3";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(unauthorizedAccessToken, orderId, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_도면_항목_추가_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();
        final String orderId = "3";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(refreshToken, orderId, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_도면_항목_추가_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "3";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(expiredAccessToken, orderId, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_도면_항목_추가_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "3";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(invalidToken, orderId, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    public void 거래_도면_항목_추가_실패_거래_접근_권한() throws Exception {
        // given
        final String accessTokenOfUser2 = jwtBuilder.accessJwtBuildOfUser2();
        final String orderId = "3";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(accessTokenOfUser2, orderId, request);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래 도면 항목 추가 실패
     * - 실패 사유 : 거래 단계가 도면 항목 추가 가능 단계(견적 대기, 견적 승인, 제작 중)가 아님
     */
    @Test
    public void 거래_도면_항목_추가_실패_거래도면추가_가능단계() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "1";
        final CustomerCreateDrawingRequest request = CustomerCreateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrderDrawing(accessToken, orderId, request);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래 도면 항목 수정 성공
     */
    @Test
    public void 거래_도면_항목_수정_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "3";
        final String drawingId = "3";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(accessToken, orderId, drawingId, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_도면_항목_수정_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "3";
        final String drawingId = "3";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawingWithOutAccessToken(orderId, drawingId, request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_도면_항목_수정_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "3";
        final String drawingId = "3";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(unauthorizedAccessToken, orderId, drawingId, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_도면_항목_수정_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();
        final String orderId = "3";
        final String drawingId = "3";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(refreshToken, orderId, drawingId, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_도면_항목_수정_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "3";
        final String drawingId = "3";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(expiredAccessToken, orderId, drawingId, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_도면_항목_수정_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "3";
        final String drawingId = "3";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(invalidToken, orderId, drawingId, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    public void 거래_도면_항목_수정_실패_거래_접근_권한() throws Exception {
        // given
        final String accessTokenOfUser2 = jwtBuilder.accessJwtBuildOfUser2();
        final String orderId = "3";
        final String drawingId = "3";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(accessTokenOfUser2, orderId, drawingId, request);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래 도면 항목 수정 실패
     * - 실패 사유 : 거래 단계가 도면 항목 수정 가능 단계(견적 대기, 견적 승인, 제작 중)가 아님
     */
    @Test
    public void 거래_도면_항목_수정_실패_거래도면항목수정_가능단계() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "1";
        final String drawingId = "3";
        final CustomerUpdateDrawingRequest request = CustomerUpdateDrawingRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderDrawing(accessToken, orderId, drawingId, request);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래 도면 삭제 성공
     */
    @Test
    public void 거래_도면_삭제_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "5";
        final String drawingId = "16";

        // when
        final ResultActions resultActions = requestDeleteOrderDrawing(accessToken, orderId, drawingId);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 도면 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_도면_삭제_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "5";
        final String drawingId = "16";

        // when
        final ResultActions resultActions = requestDeleteOrderDrawingWithOutAccessToken(orderId, drawingId);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래 도면 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_도면_삭제_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "5";
        final String drawingId = "16";

        // when
        final ResultActions resultActions = requestDeleteOrderDrawing(unauthorizedAccessToken, orderId, drawingId);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래 도면 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_도면_삭제_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();
        final String orderId = "5";
        final String drawingId = "16";

        // when
        final ResultActions resultActions = requestDeleteOrderDrawing(refreshToken, orderId, drawingId);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 도면 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_도면_삭제_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "5";
        final String drawingId = "16";

        // when
        final ResultActions resultActions = requestDeleteOrderDrawing(expiredAccessToken, orderId, drawingId);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 도면 삭제 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_도면_삭제_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "5";
        final String drawingId = "16";

        // when
        final ResultActions resultActions = requestDeleteOrderDrawing(invalidToken, orderId, drawingId);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 도면 삭제 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    public void 거래_도면_삭제_실패_거래_접근_권한() throws Exception {
        // given
        final String accessTokenOfUser2 = jwtBuilder.accessJwtBuildOfUser2();
        final String orderId = "5";
        final String drawingId = "16";

        // when
        final ResultActions resultActions = requestDeleteOrderDrawing(accessTokenOfUser2, orderId, drawingId);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래 도면 삭제 실패
     * - 실패 사유 : 거래 단계가 도면 삭제 가능 단계(견적 대기, 견적 승인, 제작 중)가 아님
     */
    @Test
    public void 거래_도면_삭제_실패_도면삭제_가능단계() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "1";
        final String drawingId = "1";

        // when
        final ResultActions resultActions = requestDeleteOrderDrawing(accessToken, orderId, drawingId);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래 도면 삭제 실패
     * - 실패 사유 : 거래 마지막 도면을 삭제할 수 없음
     */
    @Test
    public void 거래_도면_삭제_실패_마지막도면_삭제() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "3";
        final String drawingId = "3";

        // when
        final ResultActions resultActions = requestDeleteOrderDrawing(accessToken, orderId, drawingId);

        // then
        assertError(OrderErrorCode.LAST_DRAWING_DELETE, resultActions);
    }

    /**
     * 거래 견적서 승인 성공
     */
    @Test
    public void 거래_견적서_승인_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfUser2();
        final String orderId = "10";

        // when
        final ResultActions resultActions = requestApproveQuotation(accessToken, orderId);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 견적서 승인 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_견적서_승인_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "10";

        // when
        final ResultActions resultActions = requestApproveQuotationWithOutAccessToken(orderId);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래 견적서 승인 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_견적서_승인_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "10";

        // when
        final ResultActions resultActions = requestApproveQuotation(unauthorizedAccessToken, orderId);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래 견적서 승인 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_견적서_승인_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();
        final String orderId = "10";

        // when
        final ResultActions resultActions = requestApproveQuotation(refreshToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 견적서 승인 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_견적서_승인_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "10";

        // when
        final ResultActions resultActions = requestApproveQuotation(expiredAccessToken, orderId);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 견적서 승인 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_견적서_승인_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "10";

        // when
        final ResultActions resultActions = requestApproveQuotation(invalidToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 견적서 승인 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    public void 거래_견적서_승인_실패_거래_접근_권한() throws Exception {
        // given
        final String accessTokenOfUser1 = jwtBuilder.accessJwtBuild();
        final String orderId = "10";

        // when
        final ResultActions resultActions = requestApproveQuotation(accessTokenOfUser1, orderId);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래 견적서 승인 실패
     * - 실패 사유 : 거래 단계가 견적서 승인 가능 단계(견적 대기)가 아님
     */
    @Test
    public void 거래_견적서_승인_실패_견적서승인_가능단계() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "4";

        // when
        final ResultActions resultActions = requestApproveQuotation(accessToken, orderId);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.QUOTE_APPROVAL.getValue());
    }

    /**
     * 거래 견적서 승인 실패
     * - 실패 사유 : 거래 견적서가 존재하지 않음
     */
    @Test
    public void 거래_견적서_승인_실패_견적서_존재() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "5";

        // when
        final ResultActions resultActions = requestApproveQuotation(accessToken, orderId);

        // then
        assertError(OrderErrorCode.NOT_FOUND_QUOTATION, resultActions);
    }

    /**
     * 거래 발주서 작성 성공
     */
    @Test
    public void 거래_발주서_작성_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "4";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();
        final CustomerCreateOrUpdateOrderPurchaseOrderResponse expectedResponse = CustomerCreateOrUpdateOrderPurchaseOrderResponseBuilder.createBuild();

        // stub
        when(s3Service.upload(any(), (MultipartFile) any(), eq("purchase-order.png"))).thenReturn("purchase-order-url.png");

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, orderId, file, request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final CustomerCreateOrUpdateOrderPurchaseOrderResponse actualResponse = objectMapper.readValue(responseString, CustomerCreateOrUpdateOrderPurchaseOrderResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 발주서 수정 성공
     */
    @Test
    public void 거래_발주서_수정_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfUser2();
        final String orderId = "9";
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.updateBuild();
        final CustomerCreateOrUpdateOrderPurchaseOrderResponse expectedResponse = CustomerCreateOrUpdateOrderPurchaseOrderResponseBuilder.updateBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrderWithOutFile(accessToken, orderId, request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final CustomerCreateOrUpdateOrderPurchaseOrderResponse actualResponse = objectMapper.readValue(responseString, CustomerCreateOrUpdateOrderPurchaseOrderResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 발주서 작성 및 수정 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_발주서_작성및수정_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "4";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrderWithOutAccessToken(orderId, file, request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래 발주서 작성 및 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_발주서_작성및수정_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "4";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(unauthorizedAccessToken, orderId, file, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }


    /**
     * 거래 발주서 작성 및 수정 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_발주서_작성및수정_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuild();
        final String orderId = "4";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(refreshToken, orderId, file, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 발주서 작성 및 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_발주서_작성및수정_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "4";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(expiredAccessToken, orderId, file, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 발주서 작성 및 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_발주서_작성및수정_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "4";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(invalidToken, orderId, file, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 발주서 작성 및 수정 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    public void 거래_발주서_작성및수정_실패_거래_접근_권한() throws Exception {
        // given
        final String accessTokenOfUser2 = jwtBuilder.accessJwtBuildOfUser2();
        final String orderId = "4";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessTokenOfUser2, orderId, file, request);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래 발주서 작성 및 수정 실패
     * - 실패 사유 : 존재하지 않는 거래
     */
    @Test
    public void 거래_발주서_작성및수정_실패_거래_존재() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String unknownOrderId = "0";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, unknownOrderId, file, request);

        // then
        assertError(OrderErrorCode.NOT_FOUND_ORDER, resultActions);
    }

    /**
     * 거래 발주서 작성 및 수정 실패
     * - 실패 사유 : 거래 단계가 발주서 작성 및 수정 가능 단계(견적 승인)가 아님
     */
    @Test
    public void 거래_발주서_작성및수정_실패_발주서작성및수정_가능단계() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "3";
        final String filePath = "src/test/resources/purchase-order/purchase-order.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "purchase-order.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrder(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.IN_PRODUCTION.getValue());
    }

    /**
     * 거래 발주서 작성 실패
     * - 실패 사유 : 발주서 작성 시, 요청에 발주서의 파일이 존재하지 않음
     */
    @Test
    public void 거래_발주서_작성및수정_실패_발주서_파일_존재() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "4";
        final CustomerCreateOrUpdateOrderPurchaseOrderRequest request = CustomerCreateOrUpdateOrderPurchaseOrderRequestBuilder.createBuild();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderPurchaseOrderWithOutFile(accessToken, orderId, request);

        // then
        assertError(OrderErrorCode.REQUIRED_PURCHASE_ORDER_FILE, resultActions);
    }

    private ResultActions requestCreateOrder(String accessToken, CustomerCreateOrderRequest request) throws Exception {
        return mvc.perform(post("/customer/order")
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestCreateOrderWithOutAccessToken(CustomerCreateOrderRequest request) throws Exception {
        return mvc.perform(post("/customer/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestUpdateOrderDeliveryAddress(String accessToken, String orderId, CustomerUpdateOrderDeliveryAddressRequest request) throws Exception {
        return mvc.perform(patch("/customer/order/{order-id}/delivery-address", orderId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestUpdateOrderDeliveryAddressWithOutAccessToken(String orderId, CustomerUpdateOrderDeliveryAddressRequest request) throws Exception {
        return mvc.perform(patch("/customer/order/{order-id}/delivery-address", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestCreateOrderDrawing(String accessToken, String orderId, CustomerCreateDrawingRequest request) throws Exception {
        return mvc.perform(post("/customer/order/{order-id}/drawing", orderId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestCreateOrderDrawingWithOutAccessToken(String orderId, CustomerCreateDrawingRequest request) throws Exception {
        return mvc.perform(post("/customer/order/{order-id}/drawing", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestUpdateOrderDrawing(String accessToken, String orderId, String drawingId, CustomerUpdateDrawingRequest request) throws Exception {
        return mvc.perform(patch("/customer/order/{order-id}/drawing/{drawing-id}", orderId, drawingId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestUpdateOrderDrawingWithOutAccessToken(String orderId, String drawingId, CustomerUpdateDrawingRequest request) throws Exception {
        return mvc.perform(patch("/customer/order/{order-id}/drawing/{drawing-id}", orderId, drawingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestDeleteOrderDrawing(String accessToken, String orderId, String drawingId) throws Exception {
        return mvc.perform(delete("/customer/order/{order-id}/drawing/{drawing-id}", orderId, drawingId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestDeleteOrderDrawingWithOutAccessToken(String orderId, String drawingId) throws Exception {
        return mvc.perform(delete("/customer/order/{order-id}/drawing/{drawing-id}", orderId, drawingId))
                .andDo(print());
    }

    private ResultActions requestApproveQuotation(String accessToken, String orderId) throws Exception {
        return mvc.perform(patch("/customer/order/{order-id}/quotation", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestApproveQuotationWithOutAccessToken(String orderId) throws Exception {
        return mvc.perform(patch("/customer/order/{order-id}/quotation", orderId))
                .andDo(print());
    }

    private ResultActions requestCreateOrUpdateOrderPurchaseOrder(String accessToken, String orderId, MockMultipartFile file, CustomerCreateOrUpdateOrderPurchaseOrderRequest request) throws Exception {
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/customer/order/{order-id}/purchase-order", orderId);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile purchaseOrder = new MockMultipartFile("purchaseOrder", "purchaseOrder", MediaType.APPLICATION_JSON_VALUE, requestJson.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(builder
                        .file(file)
                        .file(purchaseOrder)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestCreateOrUpdateOrderPurchaseOrderWithOutFile(String accessToken, String orderId, CustomerCreateOrUpdateOrderPurchaseOrderRequest request) throws Exception {
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/customer/order/{order-id}/purchase-order", orderId);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile purchaseOrder = new MockMultipartFile("purchaseOrder", "purchaseOrder", MediaType.APPLICATION_JSON_VALUE, requestJson.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(builder
                        .file(purchaseOrder)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestCreateOrUpdateOrderPurchaseOrderWithOutAccessToken(String orderId, MockMultipartFile file, CustomerCreateOrUpdateOrderPurchaseOrderRequest request) throws Exception {
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/customer/order/{order-id}/purchase-order", orderId);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile purchaseOrder = new MockMultipartFile("purchaseOrder", "purchaseOrder", MediaType.APPLICATION_JSON_VALUE, requestJson.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(builder
                        .file(file)
                        .file(purchaseOrder)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
