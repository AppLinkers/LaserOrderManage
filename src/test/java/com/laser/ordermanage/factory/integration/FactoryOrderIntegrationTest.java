package com.laser.ordermanage.factory.integration;

import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.factory.dto.request.*;
import com.laser.ordermanage.factory.dto.response.*;
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

public class FactoryOrderIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    /**
     * 거래 긴급 설정 성공
     */
    @Test
    public void 거래_긴급_설정_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "2";
        final FactoryUpdateOrderIsUrgentRequest request = FactoryUpdateOrderIsUrgentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderIsUrgent(accessToken, orderId, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 긴급 설정 실패
     * 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_긴급_설정_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "2";
        final FactoryUpdateOrderIsUrgentRequest request = FactoryUpdateOrderIsUrgentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderIsUrgentWithOutAccessToken(orderId, request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래 긴급 설정 실패
     * 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_긴급_설정_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "2";
        final FactoryUpdateOrderIsUrgentRequest request = FactoryUpdateOrderIsUrgentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderIsUrgent(unauthorizedAccessToken, orderId, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래 긴급 설정 실패
     * 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_긴급_설정_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();
        final String orderId = "2";
        final FactoryUpdateOrderIsUrgentRequest request = FactoryUpdateOrderIsUrgentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderIsUrgent(refreshToken, orderId, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 긴급 설정 실패
     * 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_긴급_설정_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "2";
        final FactoryUpdateOrderIsUrgentRequest request = FactoryUpdateOrderIsUrgentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderIsUrgent(expiredAccessToken, orderId, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 긴급 설정 실패
     * 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_긴급_설정_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "2";
        final FactoryUpdateOrderIsUrgentRequest request = FactoryUpdateOrderIsUrgentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderIsUrgent(invalidToken, orderId, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 긴급 설정 실패
     * 실패 사유 : 거래 단계가 거래 긴급 설정 가능 단계(견적 대기, 견적 승인, 제작 중, 제작 완료)가 아님
     */
    @Test
    public void 거래_긴급_설정_실패_거래긴급설정_가능단계() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "1";
        final FactoryUpdateOrderIsUrgentRequest request = FactoryUpdateOrderIsUrgentRequestBuilder.build();

        // when
        final ResultActions resultActions = requestUpdateOrderIsUrgent(accessToken, orderId, request);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래 견적서 작성 성공
     */
    @Test
    public void 거래_견적서_작성_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "5";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();
        final FactoryCreateOrUpdateOrderQuotationResponse expectedResponse = FactoryCreateOrUpdateOrderQuotationResponseBuilder.createBuild();

        // stub
        when(s3Service.upload(any(), (MultipartFile) any(), eq("quotation.xlsx"))).thenReturn("quotation-url.xlsx");

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(accessToken, orderId, file, request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final FactoryCreateOrUpdateOrderQuotationResponse actualResponse = objectMapper.readValue(responseString, FactoryCreateOrUpdateOrderQuotationResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 견적서 수정 성공
     */
    @Test
    public void 거래_견적서_수정_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "10";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();
        final FactoryCreateOrUpdateOrderQuotationResponse expectedResponse = FactoryCreateOrUpdateOrderQuotationResponseBuilder.updateBuild();

        // stub
        when(s3Service.upload(any(), (MultipartFile) any(), eq("quotation.xlsx"))).thenReturn("quotation-url.xlsx");

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(accessToken, orderId, file, request);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final FactoryCreateOrUpdateOrderQuotationResponse actualResponse = objectMapper.readValue(responseString, FactoryCreateOrUpdateOrderQuotationResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래 견적서 작성 및 수정 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_견적서_작성및수정_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "5";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotationWithOutAccessToken(orderId, file, request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래 견적서 작성 및 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_견적서_작성및수정_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "5";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(unauthorizedAccessToken, orderId, file, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래 견적서 작성 및 수정 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_견적서_작성및수정_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();
        final String orderId = "5";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(refreshToken, orderId, file, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 견적서 작성 및 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_견적서_작성및수정_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "5";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(expiredAccessToken, orderId, file, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 견적서 작성 및 수정 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_견적서_작성및수정_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "5";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(invalidToken, orderId, file, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 견적서 작성 및 수정 실패
     * - 실패 사유 : 존재하지 않는 거래
     */
    @Test
    public void 거래_견적서_작성및수정_실패_거래_존재() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String unknownOrderId = "0";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(accessToken, unknownOrderId, file, request);

        // then
        assertError(OrderErrorCode.NOT_FOUND_ORDER, resultActions);
    }

    /**
     * 거래 견적서 작성 및 수정 실패
     * - 실패 사유 : 거래 단계가 거래 견적서 작성 및 수정 가능 단계(견적 대기)가 아님
     */
    @Test
    public void 거래_견적서_작성및수정_실패_견적서작성및수정_가능단계() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "4";
        final String filePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotation(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.QUOTE_APPROVAL.getValue());
    }

    /**
     * 거래 견적서 작성 실패
     * - 실패 사유 : 견적서 작성 시, 요청에 견적서의 파일이 존재하지 않음
     */
    @Test
    public void 거래_견적서_작성_실패_견적서_파일_존재() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "5";
        final FactoryCreateOrUpdateOrderQuotationRequest request = FactoryCreateOrUpdateOrderQuotationRequestBuilder.build();

        // when
        final ResultActions resultActions = requestCreateOrUpdateOrderQuotationWithOutFile(accessToken, orderId, request);

        // then
        assertError(OrderErrorCode.REQUIRED_QUOTATION_FILE, resultActions);
    }

    /**
     * 거래 발주서 승인 성공
     */
    @Test
    public void 거래_발주서_승인_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "9";

        // when
        final ResultActions resultActions = requestApprovePurchaseOrder(accessToken, orderId);
        
        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 발주서 승인 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_발주서_승인_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "9";
        
        // when
        final ResultActions resultActions = requestApprovePurchaseOrderWithOutAccessToken(orderId);
        
        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래 발주서 승인 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_발주서_승인_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "9";
        
        // when
        final ResultActions resultActions = requestApprovePurchaseOrder(unauthorizedAccessToken, orderId);
        
        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래 발주서 승인 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_발주서_승인_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();
        final String orderId = "9";

        // when
        final ResultActions resultActions = requestApprovePurchaseOrder(refreshToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 발주서 승인 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_발주서_승인_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "9";

        // when
        final ResultActions resultActions = requestApprovePurchaseOrder(expiredAccessToken, orderId);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 발주서 승인 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_발주서_승인_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "9";

        // when
        final ResultActions resultActions = requestApprovePurchaseOrder(invalidToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 발주서 승인 실패
     * - 실패 사유 : 거래 단계가 발주서 승인 가능 단계(견적 승인)가 아님
     */
    @Test
    public void 거래_발주서_승인_실패_발주서승인_가능단계() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "3";

        // when
        final ResultActions resultActions = requestApprovePurchaseOrder(accessToken, orderId);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.IN_PRODUCTION.getValue());
    }

    /**
     * 거래 발주서 승인 실패
     * - 실패 사유 : 거래 발주서가 존재하지 않음
     */
    @Test
    public void 거래_발주서_승인_실패_발주서_존재() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "4";

        // when
        final ResultActions resultActions = requestApprovePurchaseOrder(accessToken, orderId);

        // then
        assertError(OrderErrorCode.NOT_FOUND_PURCHASE_ORDER, resultActions);
    }

    /**
     * 거래 제작 완료 성공
     */
    @Test
    public void 거래_제작_완료_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "3";

        // when
        final ResultActions resultActions = requestChangeStageToProductionComplete(accessToken, orderId);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 제작 완료 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_제작_완료_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "3";

        // when
        final ResultActions resultActions = requestChangeStageToProductionCompletedWithOutAccessToken(orderId);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래 제작 완료 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_제작_완료_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "3";

        // when
        final ResultActions resultActions = requestChangeStageToProductionComplete(unauthorizedAccessToken, orderId);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래 제작 완료 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_제작_완료_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();
        final String orderId = "3";

        // when
        final ResultActions resultActions = requestChangeStageToProductionComplete(refreshToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 제작 완료 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_제작_완료_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "3";

        // when
        final ResultActions resultActions = requestChangeStageToProductionComplete(expiredAccessToken, orderId);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 제작 완료 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_제작_완료_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "3";

        // when
        final ResultActions resultActions = requestChangeStageToProductionComplete(invalidToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 제작 완료 실패
     * - 실패 사유 : 거래 단계가 제작 완료 가능 단계(제작 중)가 아님
     */
    @Test
    public void 거래_제작_완료_실패_제작완료_가능단계() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "2";

        // when
        final ResultActions resultActions = requestChangeStageToProductionComplete(accessToken, orderId);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.PRODUCTION_COMPLETED.getValue());
    }

    /**
     * 거래 완료 - 이메일로 인수자 확인 및 서명 링크 전송 성공
     */
    @Test
    public void 거래_완료_이메일로_인수자_확인및서명링크_전송_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "2";
        final String baseUrl = "https://base-url.com";

        // when
        final ResultActions resultActions = requestSendEmailForAcquirer(accessToken, orderId, baseUrl);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 완료 - 이메일로 인수자 확인 및 서명 링크 전송 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_완료_이메일로_인수자_확인및서명링크_전송_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "2";
        final String baseUrl = "https://base-url.com";

        // when
        final ResultActions resultActions = requestSendEmailForAcquirerWithOutAccessToken(orderId, baseUrl);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래 완료 - 이메일로 인수자 확인 및 서명 링크 전송 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_완료_이메일로_인수자_확인및서명링크_전송_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "2";
        final String baseUrl = "https://base-url.com";

        // when
        final ResultActions resultActions = requestSendEmailForAcquirer(unauthorizedAccessToken, orderId, baseUrl);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래 완료 - 이메일로 인수자 확인 및 서명 링크 전송 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_완료_이메일로_인수자_확인및서명링크_전송_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();
        final String orderId = "2";
        final String baseUrl = "https://base-url.com";

        // when
        final ResultActions resultActions = requestSendEmailForAcquirer(refreshToken, orderId, baseUrl);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 완료 - 이메일로 인수자 확인 및 서명 링크 전송 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_완료_이메일로_인수자_확인및서명링크_전송_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "2";
        final String baseUrl = "https://base-url.com";

        // when
        final ResultActions resultActions = requestSendEmailForAcquirer(expiredAccessToken, orderId, baseUrl);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 완료 - 이메일로 인수자 확인 및 서명 링크 전송 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_완료_이메일로_인수자_확인및서명링크_전송_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "2";
        final String baseUrl = "https://base-url.com";

        // when
        final ResultActions resultActions = requestSendEmailForAcquirer(invalidToken, orderId, baseUrl);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 완료 - 이메일로 인수자 확인 및 서명 링크 전송 실패
     * 실패 사유 : 거래 단계가 거래 완료 가능 단계(제작 완료)가 아님
     */
    @Test
    public void 거래_완료_이메일로_인수자_확인및서명링크_전송_실패_거래완료_가능단계() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "1";
        final String baseUrl = "https://base-url.com";

        // when
        final ResultActions resultActions = requestSendEmailForAcquirer(accessToken, orderId, baseUrl);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 성공
     */
    @Test
    public void 거래_완료_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "7";
        final String filePath = "src/test/resources/acquirer-signature/acquirer-signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "acquirer-signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        // stub
        when(s3Service.upload(any(), (MultipartFile) any(), eq("acquirer-signature.png"))).thenReturn("acquirer-signature-url.png");

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(accessToken, orderId, file, request);

        // then
        resultActions.andExpect(status().isOk());
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_완료_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "7";
        final String filePath = "src/test/resources/acquirer-signature/acquirer-signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "acquirer-signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangeStageToCompletedWithOutAccessToken(orderId, file, request);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_완료_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "7";
        final String filePath = "src/test/resources/acquirer-signature/acquirer-signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "acquirer-signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(unauthorizedAccessToken, orderId, file, request);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_완료_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();
        final String orderId = "7";
        final String filePath = "src/test/resources/acquirer-signature/acquirer-signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "acquirer-signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(refreshToken, orderId, file, request);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_완료_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "7";
        final String filePath = "src/test/resources/acquirer-signature/acquirer-signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "acquirer-signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(expiredAccessToken, orderId, file, request);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_완료_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "7";
        final String filePath = "src/test/resources/acquirer-signature/acquirer-signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "acquirer-signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(invalidToken, orderId, file, request);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 존재하지 않는 거래
     */
    @Test
    public void 거래_완료_실패_거래_존재() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String unknownOrderId = "0";
        final String filePath = "src/test/resources/acquirer-signature/acquirer-signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "acquirer-signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(accessToken, unknownOrderId, file, request);

        // then
        assertError(OrderErrorCode.NOT_FOUND_ORDER, resultActions);
    }

    /**
     * 거래 완료 - 인수자 정보 및 서명 등록 후, 거래 완료 실패
     * - 실패 사유 : 거래 단계가 거래 완료 가능 단계(제작 완료)가 아님
     */
    @Test
    public void 거래_완료_실패_거래완료_가능_단계() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "1";
        final String filePath = "src/test/resources/acquirer-signature/acquirer-signature.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "acquirer-signature.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final FactoryCreateOrderAcquirerRequest request = FactoryCreateOrderAcquirerRequestBuilder.build();

        // when
        final ResultActions resultActions = requestChangeStageToCompleted(accessToken, orderId, file, request);

        // then
        assertErrorWithMessage(OrderErrorCode.INVALID_ORDER_STAGE, resultActions, Stage.COMPLETED.getValue());
    }

    /**
     * 거래의 고객 정보 조회 성공
     */
    @Test
    public void 거래_고객_정보_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "1";
        final FactoryGetOrderCustomerResponse expectedResponse = FactoryGetOrderCustomerResponseBuilder.build();

        // when
        final ResultActions resultActions = requestGetOrderCustomer(accessToken, orderId);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final FactoryGetOrderCustomerResponse actualResponse = objectMapper.readValue(responseString, FactoryGetOrderCustomerResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래의 고객 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_고객_정보_조회_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderCustomerWithOutAccessToken(orderId);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래의 고객 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_고객_정보_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderCustomer(unauthorizedAccessToken, orderId);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래의 고객 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_고객_정보_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderCustomer(refreshToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래의 고객 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_고객_정보_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderCustomer(expiredAccessToken, orderId);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래의 고객 정보 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_고객_정보_조회_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderCustomer(invalidToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래의 발주서 파일 조회 성공
     */
    @Test
    public void 거래_발주서_파일_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "1";
        final FactoryGetPurchaseOrderFileResponse expectedResponse = FactoryGetPurchaseOrderFileResponseBuilder.build();

        // when
        final ResultActions resultActions = requestGetOrderPurchaseOrderFile(accessToken, orderId);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final FactoryGetPurchaseOrderFileResponse actualResponse = objectMapper.readValue(responseString, FactoryGetPurchaseOrderFileResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래의 발주서 파일 조회 실패
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 거래_발주서_파일_조회_실패_Header_Authorization_존재() throws Exception {
        // given
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderPurchaseOrderFileWithOutAccessToken(orderId);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 거래의 발주서 파일 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 거래_발주서_파일_조회_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderPurchaseOrderFile(unauthorizedAccessToken, orderId);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 거래의 발주서 파일 조회 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 거래_발주서_파일_조회_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfFactory();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderPurchaseOrderFile(refreshToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 거래의 발주서 파일 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 거래_발주서_파일_조회_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderPurchaseOrderFile(expiredAccessToken, orderId);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 거래의 발주서 파일 조회 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 거래_발주서_파일_조회_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderPurchaseOrderFile(invalidToken, orderId);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 거래의 발주서 파일 조회 실패
     * - 실패 사유 : 거래 발주서가 존재하지 않음
     */
    @Test
    public void 거래_발주서_파일_조회_실패_발주서_존재() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfFactory();
        final String orderId = "4";

        // when
        final ResultActions resultActions = requestGetOrderPurchaseOrderFile(accessToken, orderId);

        // then
        assertError(OrderErrorCode.NOT_FOUND_PURCHASE_ORDER, resultActions);
    }

    private ResultActions requestUpdateOrderIsUrgent(String accessToken, String orderId, FactoryUpdateOrderIsUrgentRequest request) throws Exception {
        return mvc.perform(patch("/factory/order/{order-id}/urgent", orderId)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestUpdateOrderIsUrgentWithOutAccessToken(String orderId, FactoryUpdateOrderIsUrgentRequest request) throws Exception {
        return mvc.perform(patch("/factory/order/{order-id}/urgent", orderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print());
    }

    private ResultActions requestCreateOrUpdateOrderQuotation(String accessToken, String orderId, MockMultipartFile file, FactoryCreateOrUpdateOrderQuotationRequest request) throws Exception {
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/factory/order/{order-id}/quotation", orderId);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile quotation = new MockMultipartFile("quotation", "quotation", MediaType.APPLICATION_JSON_VALUE, requestJson.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(builder
                        .file(file)
                        .file(quotation)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestCreateOrUpdateOrderQuotationWithOutFile(String accessToken, String orderId, FactoryCreateOrUpdateOrderQuotationRequest request) throws Exception {
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/factory/order/{order-id}/quotation", orderId);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile quotation = new MockMultipartFile("quotation", "quotation", MediaType.APPLICATION_JSON_VALUE, requestJson.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(builder
                        .file(quotation)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestCreateOrUpdateOrderQuotationWithOutAccessToken(String orderId, MockMultipartFile file, FactoryCreateOrUpdateOrderQuotationRequest request) throws Exception {
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/factory/order/{order-id}/quotation", orderId);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile quotation = new MockMultipartFile("quotation", "quotation", MediaType.APPLICATION_JSON_VALUE, requestJson.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(builder
                        .file(file)
                        .file(quotation)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestApprovePurchaseOrder(String accessToken, String orderId) throws Exception {
        return mvc.perform(patch("/factory/order/{order-id}/purchase-order", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestApprovePurchaseOrderWithOutAccessToken(String orderId) throws Exception {
        return mvc.perform(patch("/factory/order/{order-id}/purchase-order", orderId))
                .andDo(print());
    }

    private ResultActions requestChangeStageToProductionComplete(String accessToken, String orderId) throws Exception {
        return mvc.perform(patch("/factory/order/{order-id}/stage/production-completed", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestChangeStageToProductionCompletedWithOutAccessToken(String orderId) throws Exception {
        return mvc.perform(patch("/factory/order/{order-id}/stage/production-completed", orderId))
                .andDo(print());
    }

    private ResultActions requestSendEmailForAcquirer(String accessToken, String orderId, String baseUrl) throws Exception {
        return mvc.perform(post("/factory/order/{order-id}/acquirer/email-link", orderId)
                        .header("Authorization", "Bearer " + accessToken)
                        .param("base-url", baseUrl))
                .andDo(print());
    }

    private ResultActions requestSendEmailForAcquirerWithOutAccessToken(String orderId, String baseUrl) throws Exception {
        return mvc.perform(post("/factory/order/{order-id}/acquirer/email-link", orderId)
                        .param("base-url", baseUrl))
                .andDo(print());
    }

    private ResultActions requestChangeStageToCompleted(String accessToken, String orderId, MockMultipartFile file, FactoryCreateOrderAcquirerRequest request) throws Exception{
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/factory/order/{order-id}/stage/completed", orderId);

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile acquire = new MockMultipartFile("acquirer", "acquirer", MediaType.APPLICATION_JSON_VALUE, requestJson.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(builder
                        .file(file)
                        .file(acquire)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestChangeStageToCompletedWithOutFile(String accessToken, String orderId, FactoryCreateOrderAcquirerRequest request) throws Exception {
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/factory/order/{order-id}/stage/completed", orderId);

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile acquire = new MockMultipartFile("acquire", "acquire", MediaType.APPLICATION_JSON_VALUE, requestJson.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(builder
                        .file(acquire)
                        .header("Authorization", "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestChangeStageToCompletedWithOutAccessToken(String orderId, MockMultipartFile file, FactoryCreateOrderAcquirerRequest request) throws Exception {
        MockMultipartHttpServletRequestBuilder builder = MockMvcRequestBuilders.multipart("/factory/order/{order-id}/stage/completed", orderId);

        String requestJson = objectMapper.writeValueAsString(request);
        MockMultipartFile acquire = new MockMultipartFile("acquire", "acquire", MediaType.APPLICATION_JSON_VALUE, requestJson.getBytes(StandardCharsets.UTF_8));

        return mvc.perform(builder
                        .file(file)
                        .file(acquire)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }

    private ResultActions requestGetOrderCustomer(String accessToken, String orderId) throws Exception {
        return mvc.perform(get("/factory/order/{order-id}/customer", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderCustomerWithOutAccessToken(String orderId) throws Exception {
        return mvc.perform(get("/factory/order/{order-id}/customer", orderId))
                .andDo(print());
    }

    private ResultActions requestGetOrderPurchaseOrderFile(String accessToken, String orderId) throws Exception {
        return mvc.perform(get("/factory/order/{order-id}/purchase-order/file", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestGetOrderPurchaseOrderFileWithOutAccessToken(String orderId) throws Exception {
        return mvc.perform(get("/factory/order/{order-id}/purchase-order/file", orderId))
                .andDo(print());
    }
}
