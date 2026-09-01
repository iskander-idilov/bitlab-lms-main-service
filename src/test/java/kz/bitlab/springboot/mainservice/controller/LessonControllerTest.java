package kz.bitlab.springboot.mainservice.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.springboot.mainservice.config.SecurityConfig;
import kz.bitlab.springboot.mainservice.dto.request.CreateLessonRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateLessonRequest;
import kz.bitlab.springboot.mainservice.dto.response.LessonResponse;
import kz.bitlab.springboot.mainservice.service.LessonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@WebMvcTest(LessonController.class)
@Import(SecurityConfig.class)
class LessonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private LessonService lessonService;

    @Test
    void shouldCreateLesson() throws Exception {
        CreateLessonRequest request = new CreateLessonRequest();
        request.setName("Variables");
        request.setContent("Lesson content");
        request.setOrder(1);

        LessonResponse response = new LessonResponse();
        response.setId(100L);
        response.setName("Variables");
        response.setChapterId(10L);

        when(lessonService.create(eq(10L), any(CreateLessonRequest.class))).thenReturn(response);

        mockMvc.perform(post("/chapters/10/lessons")
                        .with(jwt())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100L))
                .andExpect(jsonPath("$.name").value("Variables"));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        CreateLessonRequest request = new CreateLessonRequest();
        request.setName("");
        request.setContent("Some content");
        request.setOrder(1);

        mockMvc.perform(post("/chapters/10/lessons")
                        .with(jwt())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetLessonById() throws Exception {
        LessonResponse response = new LessonResponse();
        response.setId(100L);
        response.setName("Variables");

        when(lessonService.getById(100L)).thenReturn(response);

        mockMvc.perform(get("/chapters/10/lessons/100")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100L));
    }

    @Test
    void shouldReturnNotFoundWhenLessonDoesNotExist() throws Exception {
        when(lessonService.getById(999L))
                .thenThrow(new IllegalArgumentException("Lesson not found with id: 999"));

        mockMvc.perform(get("/chapters/10/lessons/999")
                .with(jwt()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateLesson() throws Exception {
        UpdateLessonRequest request = new UpdateLessonRequest();
        request.setContent("Updated content");

        LessonResponse response = new LessonResponse();
        response.setId(100L);
        response.setContent("Updated content");

        when(lessonService.update(eq(100L), any(UpdateLessonRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/chapters/10/lessons/100")
                        .with(jwt())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated content"));
    }

    @Test
    void shouldDeleteLesson() throws Exception {
        mockMvc.perform(delete("/chapters/10/lessons/100")
                .with(jwt()))
                .andExpect(status().isNoContent());
    }
}