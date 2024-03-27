package com.laser.ordermanage.order.unit.api;

import com.laser.ordermanage.common.APIUnitTest;
import com.laser.ordermanage.common.exception.CommonErrorCode;
import com.laser.ordermanage.order.api.DrawingAPI;
import com.laser.ordermanage.order.dto.response.UploadDrawingFileResponse;
import com.laser.ordermanage.order.dto.response.UploadDrawingFileResponseBuilder;
import com.laser.ordermanage.order.service.DrawingService;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DrawingAPI.class)
public class DrawingAPIUnitTest extends APIUnitTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private DrawingService drawingService;

    @BeforeEach
    public void setUp() {
        mvc = buildMockMvc(context);
    }

    /**
     * 도면 파일 업로드 성공
     */
    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void 도면_파일_업로드_성공() throws Exception {
        // given
        final String accessToken = "access-token";
        final String filePath = "src/test/resources/drawing/drawing.dwg";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.dwg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final UploadDrawingFileResponse expectedResponse = UploadDrawingFileResponseBuilder.buildOfDWGDrawing();

        // stub
        when(drawingService.uploadDrawingFile(any())).thenReturn(expectedResponse);

        // when
        final ResultActions resultActions = requestUploadDrawingFile(accessToken, file);

        // then
        final String responseString = resultActions
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);

        final UploadDrawingFileResponse actualResponse = objectMapper.readValue(responseString, UploadDrawingFileResponse.class);

        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    /**
     * 도면 파일 업로드 실패
     * - 실패 사유 : 공장(FACTORY) 사용자에 의한 요청
     */
    @Test
    @WithMockUser(roles = "FACTORY")
    public void 도면_파일_업로드_실패_사용자_역할() throws Exception {
        // given
        final String accessToken = "access-token";
        final String filePath = "src/test/resources/drawing/drawing.dwg";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.dwg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );

        // when
        final ResultActions resultActions = requestUploadDrawingFile(accessToken, file);

        // then
        assertError(UserErrorCode.DENIED_ACCESS, resultActions);
    }

    /**
     * 도면 파일 업로드 실패
     * - 실패 사유 : 파일 파라미터 null
     */
    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void 도면_파일_업로드_파일_파라미터_null() throws Exception {
        // given
        final String accessToken = "access-token";

        // when
        final ResultActions resultActions = requestUploadDrawingFileWithOutFile(accessToken);

        // then
        assertErrorWithMessage(CommonErrorCode.REQUIRED_PARAMETER, resultActions, "file");
    }

    /**
     * 도면 파일 업로드 실패
     * - 실패 사유 : 파일 파라미터 empty
     */
    @Test
    @WithMockUser(roles = "CUSTOMER")
    public void 도면_파일_업로드_파일_파라미터_empty() throws Exception {
        // given
        final String accessToken = "access-token";
        final MockMultipartFile emptyFile = new MockMultipartFile("file", new byte[0]);

        // when
        final ResultActions resultActions = requestUploadDrawingFile(accessToken, emptyFile);

        // then
        assertErrorWithMessage(CommonErrorCode.INVALID_PARAMETER, resultActions, "도면 파일은 필수 입력값입니다.");
    }

    private ResultActions requestUploadDrawingFile(String accessToken, MockMultipartFile file) throws Exception {
        return mvc.perform(
                        multipart("/drawing")
                                .file(file)
                                .accept(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestUploadDrawingFileWithOutFile(String accessToken) throws Exception {
        return mvc.perform(
                        multipart("/drawing")
                                .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }
}
