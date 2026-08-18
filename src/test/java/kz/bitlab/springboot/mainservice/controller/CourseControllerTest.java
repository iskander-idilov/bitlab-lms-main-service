package kz.bitlab.springboot.mainservice.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import kz.bitlab.springboot.mainservice.dto.request.CreateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.response.CourseResponse;
import kz.bitlab.springboot.mainservice.service.CourseService;
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

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CourseService courseService;

    @Test
    void shouldCreateCourse() throws Exception {
        CreateCourseRequest request = new CreateCourseRequest();
        request.setName("Java Basics");
        request.setDescription("Learn Java");

        CourseResponse response = new CourseResponse();
        response.setId(1L);
        response.setName("Java Basics");
        response.setDescription("Learn Java");

        when(courseService.create(any(CreateCourseRequest.class))).thenReturn(response);

        mockMvc.perform(post("/courses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java Basics"));
    }

    @Test
    void shouldReturnBadRequestWhenNameIsBlank() throws Exception {
        CreateCourseRequest request = new CreateCourseRequest();
        request.setName("");

        mockMvc.perform(post("/courses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetCourseById() throws Exception {
        CourseResponse response = new CourseResponse();
        response.setId(1L);
        response.setName("Java Basics");

        when(courseService.getById(1L)).thenReturn(response);

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Java Basics"));
    }

    @Test
    void shouldReturnNotFoundWhenCourseDoesNotExist() throws Exception {
        when(courseService.getById(999L))
                .thenThrow(new IllegalArgumentException("Course not found with id: 999"));

        mockMvc.perform(get("/courses/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Course not found with id: 999"));
    }

    @Test
    void shouldUpdateCourse() throws Exception {
        UpdateCourseRequest request = new UpdateCourseRequest();
        request.setDescription("Updated description");

        CourseResponse response = new CourseResponse();
        response.setId(1L);
        response.setDescription("Updated description");

        when(courseService.update(eq(1L), any(UpdateCourseRequest.class))).thenReturn(response);

        mockMvc.perform(patch("/courses/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated description"));
    }

    @Test
    void shouldDeleteCourse() throws Exception {
        mockMvc.perform(delete("/courses/1"))
                .andExpect(status().isNoContent());
    }
}