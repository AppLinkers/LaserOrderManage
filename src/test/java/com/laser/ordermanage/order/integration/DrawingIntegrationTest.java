package com.laser.ordermanage.order.integration;

import com.laser.ordermanage.common.IntegrationTest;
import com.laser.ordermanage.common.security.jwt.setup.JwtBuilder;
import com.laser.ordermanage.order.dto.response.UploadDrawingFileResponse;
import com.laser.ordermanage.order.dto.response.UploadDrawingFileResponseBuilder;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.user.exception.UserErrorCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DrawingIntegrationTest extends IntegrationTest {

    @Autowired
    private JwtBuilder jwtBuilder;

    /**
     * 도면 파일 업로드 성공 (DWG)
     */
    @Test
    public void 도면_파일_업로드_성공_DWG() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();
        final String filePath = "src/test/resources/drawing/drawing.dwg";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.dwg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
                );
        final UploadDrawingFileResponse expectedResponse = UploadDrawingFileResponseBuilder.buildOfDWGDrawing();

        // stub
        when(s3Service.upload(any(), (MultipartFile) any(), eq("drawing.dwg"))).thenReturn("drawing-file-url.dwg");
        when(s3Service.upload(any(), (File) any(), eq("drawing-thumbnail.png"))).thenReturn("thumbnail-url.dwg");

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
     * 도면 파일 업로드 성공 (DXF)
     */
    @Test
    public void 도면_파일_업로드_성공_DXF() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();
        final String filePath = "src/test/resources/drawing/drawing.dxf";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.dxf",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final UploadDrawingFileResponse expectedResponse = UploadDrawingFileResponseBuilder.buildOfDXFDrawing();

        // stub
        when(s3Service.upload(any(), (MultipartFile) any(), eq("drawing.dxf"))).thenReturn("drawing-file-url.dxf");
        when(s3Service.upload(any(), (File) any(), eq("drawing-thumbnail.png"))).thenReturn("thumbnail-url.dxf");

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
     * 도면 파일 업로드 성공 (PDF)
     */
    @Test
    public void 도면_파일_업로드_성공_PDF() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();
        final String filePath = "src/test/resources/drawing/drawing.pdf";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.pdf",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final UploadDrawingFileResponse expectedResponse = UploadDrawingFileResponseBuilder.buildOfPDFDrawing();

        // stub
        when(s3Service.upload(any(), (MultipartFile) any(), eq("drawing.pdf"))).thenReturn("drawing-file-url.pdf");
        when(s3Service.upload(any(), (File) any(), eq("drawing-thumbnail.png"))).thenReturn("thumbnail-url.pdf");

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
     * 도면 파일 업로드 성공 (PNG)
     */
    @Test
    public void 도면_파일_업로드_성공_PNG() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();
        final String filePath = "src/test/resources/drawing/drawing.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final UploadDrawingFileResponse expectedResponse = UploadDrawingFileResponseBuilder.buildOfPNGDrawing();

        // stub
        when(s3Service.upload(any(), (MultipartFile) any(), eq("drawing.png"))).thenReturn("drawing-file-url.png");
        when(s3Service.upload(any(), (File) any(), eq("drawing-thumbnail.png"))).thenReturn("thumbnail-url.png");

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
     * - 실패 사유 : 요청 시, Header 에 Authorization 정보 (Access Token) 를 추가하지 않음
     */
    @Test
    public void 도면_파일_업로드_실패_Header_Authorization_존재() throws Exception {
        // given
        final String filePath = "src/test/resources/drawing/drawing.dwg";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.dwg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );

        // when
        final ResultActions resultActions = requestUploadDrawingFileWithOutAccessToken(file);

        // then
        assertError(UserErrorCode.MISSING_JWT, resultActions);
    }

    /**
     * 도면 파일 업로드 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization 정보 (Access Token) 에 권한 정보가 없음
     */
    @Test
    public void 도면_파일_업로드_실패_Unauthorized_Access_Token() throws Exception {
        // given
        final String unauthorizedAccessToken = jwtBuilder.unauthorizedAccessJwtBuild();
        final String filePath = "src/test/resources/drawing/drawing.dwg";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.dwg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );

        // when
        final ResultActions resultActions = requestUploadDrawingFile(unauthorizedAccessToken, file);

        // then
        assertError(UserErrorCode.UNAUTHORIZED_JWT, resultActions);
    }

    /**
     * 도면 파일 업로드 실패
     * - 실패 사유 : 요청 시, Header 에 다른 타입의 Authorization 정보 (Refresh Token) 를 추가함
     */
    @Test
    public void 도면_파일_업로드_실패_Token_Type() throws Exception {
        // given
        final String refreshToken = jwtBuilder.refreshJwtBuildOfCustomer();
        final String filePath = "src/test/resources/drawing/drawing.dwg";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.dwg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );

        // when
        final ResultActions resultActions = requestUploadDrawingFile(refreshToken, file);

        // then
        assertError(UserErrorCode.INVALID_TOKEN_TYPE, resultActions);
    }

    /**
     * 도면 파일 업로드 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(Access Token) 의 유효기간 만료
     */
    @Test
    public void 도면_파일_업로드_실패_Expired_Access_Token() throws Exception {
        // given
        final String expiredAccessToken = jwtBuilder.expiredAccessJwtBuild();
        final String filePath = "src/test/resources/drawing/drawing.dwg";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.dwg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );

        // when
        final ResultActions resultActions = requestUploadDrawingFile(expiredAccessToken, file);

        // then
        assertError(UserErrorCode.EXPIRED_JWT, resultActions);
    }

    /**
     * 도면 파일 업로드 실패
     * - 실패 사유 : 요청 시, Header 에 있는 Authorization(JWT) 가 유효하지 않음
     */
    @Test
    public void 도면_파일_업로드_실패_Invalid_Token() throws Exception {
        // given
        final String invalidToken = jwtBuilder.invalidJwtBuild();
        final String filePath = "src/test/resources/drawing/drawing.dwg";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.dwg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );

        // when
        final ResultActions resultActions = requestUploadDrawingFile(invalidToken, file);

        // then
        assertError(UserErrorCode.INVALID_JWT, resultActions);
    }

    /**
     * 도면 파일 업로드 실패
     * - 실패 사유 : 지원하지 않는 도면 파일 확장자
     */
    @Test
    public void 도면_파일_업로드_실패_Invalid_File_Extension() throws Exception {
        // given
        final String accessToken = jwtBuilder.accessJwtBuildOfCustomer();
        final String invalidFilePath = "src/test/resources/quotation/quotation.xlsx";
        final MockMultipartFile invalidFile = new MockMultipartFile(
                "file",
                "quotation.xlsx",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(invalidFilePath)
        );

        // when
        final ResultActions resultActions = requestUploadDrawingFile(accessToken, invalidFile);

        // then
        assertError(OrderErrorCode.UNSUPPORTED_DRAWING_FILE_EXTENSION, resultActions);
    }

    private ResultActions requestUploadDrawingFile(String accessToken, MockMultipartFile file) throws Exception {
        return mvc.perform(
                multipart("/drawing")
                        .file(file)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken))
                .andDo(print());
    }

    private ResultActions requestUploadDrawingFileWithOutAccessToken(MockMultipartFile file) throws Exception {
        return mvc.perform(
                        multipart("/drawing")
                                .file(file)
                                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
