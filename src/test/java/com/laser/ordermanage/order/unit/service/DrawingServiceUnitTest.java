package com.laser.ordermanage.order.unit.service;

import com.laser.ordermanage.common.ServiceUnitTest;
import com.laser.ordermanage.common.cloud.aws.S3Service;
import com.laser.ordermanage.common.exception.CustomCommonException;
import com.laser.ordermanage.order.domain.Drawing;
import com.laser.ordermanage.order.domain.DrawingBuilder;
import com.laser.ordermanage.order.domain.Order;
import com.laser.ordermanage.order.domain.OrderBuilder;
import com.laser.ordermanage.order.domain.type.DrawingFileType;
import com.laser.ordermanage.order.dto.response.UploadDrawingFileResponse;
import com.laser.ordermanage.order.dto.response.UploadDrawingFileResponseBuilder;
import com.laser.ordermanage.order.exception.OrderErrorCode;
import com.laser.ordermanage.order.repository.DrawingRepository;
import com.laser.ordermanage.order.service.DrawingService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DrawingServiceUnitTest extends ServiceUnitTest {

    @InjectMocks
    private DrawingService drawingService;

    @Mock
    private S3Service s3Service;

    @Mock
    private DrawingRepository drawingRepository;

    public void setUp() {
        ReflectionTestUtils.setField(drawingService, "tempFolderPath", "src/test/resources/temp/");
    }

    /**
     * 도면 DB id 기준으로 도면 조회 성공
     */
    @Test
    public void getDrawingById_성공() {
        // given
        final Long drawingId = 1L;
        final Drawing expectedDrawing = DrawingBuilder.build();

        // stub
        when(drawingRepository.findFirstById(drawingId)).thenReturn(Optional.of(expectedDrawing));

        // when
        final Drawing actualDrawing = drawingService.getDrawingById(drawingId);

        // then
        Assertions.assertThat(actualDrawing).isEqualTo(expectedDrawing);
    }

    /**
     * 도면 DB id 기준으로 도면 조회 실패
     * - 실패 사유 : 존재하지 않는 도면
     */
    @Test
    public void getDrawingById_실패_NOT_FOUND_DRAWING() {
        // given
        final Long unknownDrawingId = 0L;

        // stub
        when(drawingRepository.findFirstById(unknownDrawingId)).thenReturn(Optional.empty());

        // when & then
        Assertions.assertThatThrownBy(() -> drawingService.getDrawingById(unknownDrawingId))
                .isInstanceOf(CustomCommonException.class)
                .hasMessage(OrderErrorCode.NOT_FOUND_DRAWING.getMessage());
    }

    /**
     * 거래에 대한 도면 개수 조회 성공
     */
    @Test
    public void countDrawingByOrder_성공() {
        // given
        final Order order = OrderBuilder.build();
        final Integer expectedCountDrawing = 1;

        // stub
        when(drawingRepository.countByOrder(order)).thenReturn(expectedCountDrawing);

        // when
        final Integer actualCountDrawing = drawingService.countDrawingByOrder(order);

        // then
        Assertions.assertThat(actualCountDrawing).isEqualTo(expectedCountDrawing);
    }

    /**
     * 도면 파일의 썸네일 추출 기능 성공 (DWG)
     */
    @Test
    public void extractThumbnail_성공_DWG() throws Exception {
        // before
        setUp();

        // given
        final String filePath = "src/test/resources/drawing/drawing.dwg";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.dwg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );

        // when
        File thumbnailFile = drawingService.extractThumbnail(file, DrawingFileType.DWG);

        // then
        Assertions.assertThat(thumbnailFile.exists()).isTrue();
        Assertions.assertThat(thumbnailFile.isFile()).isTrue();

        // after
        thumbnailFile.delete();
    }

    /**
     * 도면 파일의 썸네일 추출 기능 성공 (DXF)
     */
    @Test
    public void extractThumbnail_성공_DXF() throws Exception {
        // before
        setUp();

        // given
        final String filePath = "src/test/resources/drawing/drawing.dxf";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.dxf",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );

        // when
        File thumbnailFile = drawingService.extractThumbnail(file, DrawingFileType.DXF);

        // then
        Assertions.assertThat(thumbnailFile.exists()).isTrue();
        Assertions.assertThat(thumbnailFile.isFile()).isTrue();

        // after
        thumbnailFile.delete();
    }

    /**
     * 도면 파일의 썸네일 추출 기능 성공 (PDF)
     */
    @Test
    public void extractThumbnail_성공_PDF() throws Exception {
        // before
        setUp();

        // given
        final String filePath = "src/test/resources/drawing/drawing.pdf";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.pdf",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );

        // when
        File thumbnailFile = drawingService.extractThumbnail(file, DrawingFileType.PDF);

        // then
        Assertions.assertThat(thumbnailFile.exists()).isTrue();
        Assertions.assertThat(thumbnailFile.isFile()).isTrue();

        // after
        thumbnailFile.delete();
    }

    /**
     * 도면 파일의 썸네일 추출 기능 성공 (PNG)
     */
    @Test
    public void extractThumbnail_성공_PNG() throws Exception {
        // before
        setUp();

        // given
        final String filePath = "src/test/resources/drawing/drawing.png";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.png",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );

        // when
        File thumbnailFile = drawingService.extractThumbnail(file, DrawingFileType.PNG);

        // then
        Assertions.assertThat(thumbnailFile.exists()).isTrue();
        Assertions.assertThat(thumbnailFile.isFile()).isTrue();

        // after
        thumbnailFile.delete();
    }

    /**
     * 썸네일 파일 S3 업로드 기능 성공
     */
    @Test
    public void uploadThumbnailFile_성공() {
        // given
        File thumbnailFile = mock(File.class);
        String expectedThumbnailFileUrl = "thumbnail-file.url";

        // stub
        when(s3Service.upload(any(), (File) any(), any())).thenReturn(expectedThumbnailFileUrl);

        // when
        String actualThumbnailFileUrl = drawingService.uploadThumbnailFile(thumbnailFile);

        // then
        Assertions.assertThat(actualThumbnailFileUrl).isEqualTo(expectedThumbnailFileUrl);
    }

    /**
     * 도면 파일 업로드 기능 성공
     */
    @Test
    public void uploadDrawingFile_성공() throws Exception {
        // before
        setUp();

        // given
        final String filePath = "src/test/resources/drawing/drawing.dwg";
        final MockMultipartFile file = new MockMultipartFile(
                "file",
                "drawing.dwg",
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(filePath)
        );
        final UploadDrawingFileResponse expectedResponse = UploadDrawingFileResponseBuilder.buildOfDWGDrawing();

        // stub
        when(s3Service.upload(any(), (MultipartFile) any(), any())).thenReturn("drawing-file-url.dwg");
        when(s3Service.upload(any(), (File) any(), any())).thenReturn("thumbnail-url.dwg");

        // when
        UploadDrawingFileResponse actualResponse = drawingService.uploadDrawingFile(file);

        // then
        Assertions.assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
