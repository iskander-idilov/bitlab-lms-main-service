package kz.bitlab.springboot.mainservice.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.springboot.mainservice.dto.request.CreateChapterRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateChapterRequest;
import kz.bitlab.springboot.mainservice.dto.response.ChapterResponse;
import kz.bitlab.springboot.mainservice.service.ChapterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChapterController.class)
class ChapterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private ChapterService chapterService;

    @Test
    void shouldCreateChapter() throws Exception {
        CreateChapterRequest request = new CreateChapterRequest();
        request.setName("Intro");
        request.setOrder(1);

        ChapterResponse response = new ChapterResponse();
        response.setId(10L);
        response.setName("Intro");
        response.setCourseId(1L);

        when(chapterService.create(eq(1L), any(CreateChapterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/courses/1/chapters")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.name").value("Intro"));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        CreateChapterRequest request = new CreateChapterRequest();
        request.setName("");
        request.setOrder(1);

        mockMvc.perform(post("/courses/1/chapters")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetChapterById() throws Exception {
        ChapterResponse response = new ChapterResponse();
        response.setId(10L);
        response.setName("Intro");

        when(chapterService.getById(10L)).thenReturn(response);

        mockMvc.perform(get("/courses/1/chapters/10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10L));
    }

    @Test
    void shouldReturnNotFoundWhenChapterDoesNotExist() throws Exception {
        when(chapterService.getById(999L))
                .thenThrow(new IllegalArgumentException("Chapter not found with id: 999"));

        mockMvc.perform(get("/courses/1/chapters/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdateChapter() throws Exception {
        UpdateChapterRequest request = new UpdateChapterRequest();
        request.setName("Updated Intro");

        ChapterResponse response = new ChapterResponse();
        response.setId(10L);
        response.setName("Updated Intro");

        when(chapterService.update(eq(10L), any(UpdateChapterRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/courses/1/chapters/10")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Intro"));
    }

    @Test
    void shouldDeleteChapter() throws Exception {
        mockMvc.perform(delete("/courses/1/chapters/10"))
                .andExpect(status().isNoContent());
    }
}