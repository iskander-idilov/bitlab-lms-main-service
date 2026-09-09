package kz.bitlab.springboot.mainservice.controller;
import kz.bitlab.springboot.mainservice.config.SecurityConfig;
import kz.bitlab.springboot.mainservice.dto.response.AttachmentResponse;
import kz.bitlab.springboot.mainservice.entity.Attachment;
import kz.bitlab.springboot.mainservice.service.AttachmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AttachmentController.class)
@Import(SecurityConfig.class)
class AttachmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AttachmentService attachmentService;

    @Test
    void shouldUploadAttachmentWhenAdminRole() throws Exception {
        AttachmentResponse response = new AttachmentResponse();
        response.setId(1L);
        response.setName("test.png");
        response.setUrl("uuid-test.png");
        response.setLessonId(1L);
        response.setCreatedTime(LocalDateTime.now());

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.png", "image/png", "content".getBytes());

        when(attachmentService.upload(eq(1L), any())).thenReturn(response);

        mockMvc.perform(multipart("/upload")
                        .file(file)
                        .param("lessonId", "1")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("test.png"));
    }

    @Test
    void shouldUploadAttachmentWhenTeacherRole() throws Exception {
        AttachmentResponse response = new AttachmentResponse();
        response.setId(2L);
        response.setName("lecture.pdf");
        response.setUrl("uuid-lecture.pdf");
        response.setLessonId(1L);
        response.setCreatedTime(LocalDateTime.now());

        MockMultipartFile file = new MockMultipartFile(
                "file", "lecture.pdf", "application/pdf", "content".getBytes());

        when(attachmentService.upload(eq(1L), any())).thenReturn(response);

        mockMvc.perform(multipart("/upload")
                        .file(file)
                        .param("lessonId", "1")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_TEACHER"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("lecture.pdf"));
    }

    @Test
    void shouldReturnForbiddenWhenUploadingWithoutAdminOrTeacherRole() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.png", "image/png", "content".getBytes());

        mockMvc.perform(multipart("/upload")
                        .file(file)
                        .param("lessonId", "1")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_STUDENT"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldDownloadAttachment() throws Exception {
        Attachment attachment = new Attachment();
        attachment.setId(1L);
        attachment.setName("test.png");
        attachment.setUrl("uuid-test.png");

        when(attachmentService.getById(1L)).thenReturn(attachment);
        when(attachmentService.download(attachment))
                .thenReturn(new ByteArrayInputStream("content".getBytes()));

        mockMvc.perform(get("/download/1")
                        .with(jwt()))
                .andExpect(status().isOk());
    }
}