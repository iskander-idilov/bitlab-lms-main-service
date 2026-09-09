package kz.bitlab.springboot.mainservice.service;
import io.minio.MinioClient;
import kz.bitlab.springboot.mainservice.config.MinioProperties;
import kz.bitlab.springboot.mainservice.dto.response.AttachmentResponse;
import kz.bitlab.springboot.mainservice.entity.Attachment;
import kz.bitlab.springboot.mainservice.entity.Lesson;
import kz.bitlab.springboot.mainservice.mapper.AttachmentMapper;
import kz.bitlab.springboot.mainservice.repository.AttachmentRepository;
import kz.bitlab.springboot.mainservice.repository.LessonRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AttachmentServiceTest {

    @Mock
    private MinioClient minioClient;

    @Mock
    private MinioProperties minioProperties;

    @Mock
    private AttachmentRepository attachmentRepository;

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private AttachmentMapper attachmentMapper;

    @InjectMocks
    private AttachmentService attachmentService;

    @Test
    void shouldUploadAttachmentSuccessfully() throws Exception {
        Long lessonId = 1L;
        Lesson lesson = new Lesson();
        lesson.setId(lessonId);

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.png", "image/png", "content".getBytes());

        Attachment savedAttachment = new Attachment();
        savedAttachment.setId(1L);
        savedAttachment.setName("test.png");
        savedAttachment.setLesson(lesson);

        AttachmentResponse expectedResponse = new AttachmentResponse();
        expectedResponse.setId(1L);
        expectedResponse.setName("test.png");

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.of(lesson));
        when(minioProperties.bucketName()).thenReturn("dev-bucket");
        when(attachmentRepository.save(any(Attachment.class))).thenReturn(savedAttachment);
        when(attachmentMapper.toResponse(savedAttachment)).thenReturn(expectedResponse);

        AttachmentResponse result = attachmentService.upload(lessonId, file);

        assertEquals("test.png", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenLessonNotFoundOnUpload() {
        Long lessonId = 999L;
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.png", "image/png", "content".getBytes());

        when(lessonRepository.findById(lessonId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> attachmentService.upload(lessonId, file));
    }

    @Test
    void shouldThrowExceptionWhenAttachmentNotFoundOnGetById() {
        Long id = 999L;
        when(attachmentRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> attachmentService.getById(id));
    }
}