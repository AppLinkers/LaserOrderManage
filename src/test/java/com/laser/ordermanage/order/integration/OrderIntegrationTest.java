package com.laser.ordermanage.order.integration;

import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.order.dto.response.GetOrderDetailResponse;
import com.laser.ordermanage.order.dto.response.GetOrderDetailResponseBuilder;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class OrderIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    /**
     * 거래의 상세 정보 조회 성공
     */
    @Test
    public void 거래_상세_정보_조회_성공() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuild();
        final String orderId = "1";
        final GetOrderDetailResponse expectedResponse = GetOrderDetailResponseBuilder.build();

        // when
        final ResultActions resultActions = requestGetOrderDetail(accessToken, orderId);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        // 응답 본문을 객체로 변환
        final GetOrderDetailResponse actualResponse = objectMapper.readValue(responseString, GetOrderDetailResponse.class);

        // 응답 객체와 예상 객체 비교
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 거래의 상세 정보 조회 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */
    @Test
    public void 거래_상세_정보_조회_실패_거래접근권한() throws Exception {
        // given
        final String accessTokenOfUser2 = jwtBuilder.accessJwtBuildOfUser2();
        final String orderId = "1";

        // when
        final ResultActions resultActions = requestGetOrderDetail(accessTokenOfUser2, orderId);

        // then
        assertError(OrderErrorCode.DENIED_ACCESS_TO_ORDER, resultActions);
    }

    /**
     * 거래의 댓글 목록 조회 성공
     */

    /**
     * 거래의 댓글 목록 조회 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */

    /**
     * 거래에 댓글 작성 성공
     */

    /**
     * 거래에 댓글 작성 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */

    /**
     * 거래 삭제 성공
     */

    /**
     * 거래 삭제 실패
     * - 실패 사유 : 거래에 대한 접근 권한이 없음
     */

    /**
     * 거래 삭제 실패
     * - 실패 사유 : 거래의 단계가 삭제 가능 단계(견적 대기, 견적 승인)가 아님
     */

    private ResultActions requestGetOrderDetail(String accessToken, String orderId) throws Exception {
        return mvc.perform(get("/order/{orderId}/detail", orderId)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }
}
