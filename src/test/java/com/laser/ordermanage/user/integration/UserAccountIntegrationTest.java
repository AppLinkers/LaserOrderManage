package com.laser.ordermanage.user.integration;

import com.laser.ordermanage.common.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserAccountIntegrationTest extends IntegrationTest {

    /**
     * 고객 이메일 찾기 성공
     */
    @Test
    public void 고객_이메일_찾기_성공() throws Exception {
        // given
        String customerName = "고객 이름 1";
        String customerPhone = "01011111111";

        // when
        final ResultActions resultActions = requestGetUserEmail(customerName, customerPhone);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("contents").isArray())
                .andExpect(jsonPath("contents.size()").value(2))
                .andExpect(jsonPath("totalElements").value(2))
                .andExpect(jsonPath("contents[0].name").value(customerName))
                .andExpect(jsonPath("contents[1].name").value(customerName));
    }

    /**
     * 공장 이메일 찾기 성공
     */
    @Test
    public void 공장_이메일_찾기_성공() throws Exception {
        // given
        String factoryName = "금오 M.T";
        String factoryPhone = "01011111111";

        // when
        final ResultActions resultActions = requestGetUserEmail(factoryName, factoryPhone);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("contents").isArray())
                .andExpect(jsonPath("contents.size()").value(1))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("contents[0].name").value(factoryName));
    }

    /**
     * 이메일 찾기 성공
     */
    @Test
    public void 이메일_찾기_성공() throws Exception {
        // given
        String name = "사용자 이름";
        String phone = "01099999999";

        // when
        final ResultActions resultActions = requestGetUserEmail(name, phone);

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("contents").isArray())
                .andExpect(jsonPath("contents.size()").value(0))
                .andExpect(jsonPath("totalElements").value(0));
    }

    private ResultActions requestGetUserEmail(String name, String phone) throws Exception {
        return mvc.perform(get("/user/email")
                        .param("name", name)
                        .param("phone", phone))
                .andDo(print());
    }
}
