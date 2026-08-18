package kz.bitlab.springboot.mainservice.service;
import kz.bitlab.springboot.mainservice.dto.request.CreateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.response.CourseResponse;
import kz.bitlab.springboot.mainservice.entity.Course;
import kz.bitlab.springboot.mainservice.mapper.CourseMapper;
import kz.bitlab.springboot.mainservice.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    @Test
    void shouldCreateCourse() {
        CreateCourseRequest request = new CreateCourseRequest();
        request.setName("Java Basics");

        Course course = new Course();
        course.setName("Java Basics");

        Course savedCourse = new Course();
        savedCourse.setId(1L);
        savedCourse.setName("Java Basics");

        CourseResponse expectedResponse = new CourseResponse();
        expectedResponse.setId(1L);
        expectedResponse.setName("Java Basics");

        when(courseMapper.toEntity(request)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(savedCourse);
        when(courseMapper.toResponse(savedCourse)).thenReturn(expectedResponse);

        CourseResponse result = courseService.create(request);

        assertEquals(1L, result.getId());
        assertEquals("Java Basics", result.getName());
    }

    @Test
    void shouldReturnCourseWhenFound() {
        Long id = 1L;
        Course course = new Course();
        course.setId(id);
        course.setName("Java Basics");

        CourseResponse expectedResponse = new CourseResponse();
        expectedResponse.setId(id);
        expectedResponse.setName("Java Basics");

        when(courseRepository.findById(id)).thenReturn(Optional.of(course));
        when(courseMapper.toResponse(course)).thenReturn(expectedResponse);

        CourseResponse result = courseService.getById(id);

        assertEquals(id, result.getId());
        assertEquals("Java Basics", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenCourseNotFoundOnGetById() {
        Long id = 999L;
        when(courseRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> courseService.getById(id));
    }

    @Test
    void shouldUpdateCourse() {
        Long id = 1L;
        UpdateCourseRequest request = new UpdateCourseRequest();
        request.setDescription("New description");

        Course course = new Course();
        course.setId(id);
        course.setName("Java Basics");
        course.setDescription("Old description");

        Course savedCourse = new Course();
        savedCourse.setId(id);
        savedCourse.setName("Java Basics");
        savedCourse.setDescription("New description");

        CourseResponse expectedResponse = new CourseResponse();
        expectedResponse.setId(id);
        expectedResponse.setDescription("New description");

        when(courseRepository.findById(id)).thenReturn(Optional.of(course));
        when(courseRepository.save(course)).thenReturn(savedCourse);
        when(courseMapper.toResponse(savedCourse)).thenReturn(expectedResponse);

        CourseResponse result = courseService.update(id, request);

        assertEquals("New description", result.getDescription());
        verify(courseMapper).updateEntity(request, course);
    }

    @Test
    void shouldThrowExceptionWhenCourseNotFoundOnUpdate() {
        Long id = 999L;
        UpdateCourseRequest request = new UpdateCourseRequest();
        when(courseRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> courseService.update(id, request));
    }

    @Test
    void shouldDeleteCourse() {
        Long id = 1L;
        when(courseRepository.existsById(id)).thenReturn(true);

        courseService.delete(id);

        verify(courseRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenCourseNotFoundOnDelete() {
        Long id = 999L;
        when(courseRepository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> courseService.delete(id));
        verify(courseRepository, never()).deleteById(any());
    }
}