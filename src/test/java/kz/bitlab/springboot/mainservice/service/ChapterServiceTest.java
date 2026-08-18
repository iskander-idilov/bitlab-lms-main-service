package kz.bitlab.springboot.mainservice.service;
import kz.bitlab.springboot.mainservice.dto.request.CreateChapterRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateChapterRequest;
import kz.bitlab.springboot.mainservice.dto.response.ChapterResponse;
import kz.bitlab.springboot.mainservice.entity.Chapter;
import kz.bitlab.springboot.mainservice.entity.Course;
import kz.bitlab.springboot.mainservice.mapper.ChapterMapper;
import kz.bitlab.springboot.mainservice.repository.ChapterRepository;
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
class ChapterServiceTest {

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ChapterMapper chapterMapper;

    @InjectMocks
    private ChapterService chapterService;

    @Test
    void shouldCreateChapter() {
        Long courseId = 1L;
        CreateChapterRequest request = new CreateChapterRequest();
        request.setName("Intro");
        request.setOrder(1);

        Course course = new Course();
        course.setId(courseId);

        Chapter chapter = new Chapter();
        chapter.setName("Intro");

        Chapter savedChapter = new Chapter();
        savedChapter.setId(10L);
        savedChapter.setName("Intro");

        ChapterResponse expectedResponse = new ChapterResponse();
        expectedResponse.setId(10L);
        expectedResponse.setName("Intro");

        when(chapterMapper.toEntity(request)).thenReturn(chapter);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(chapterRepository.save(chapter)).thenReturn(savedChapter);
        when(chapterMapper.toResponse(savedChapter)).thenReturn(expectedResponse);

        ChapterResponse result = chapterService.create(courseId, request);

        assertEquals(10L, result.getId());
        assertEquals("Intro", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenCourseNotFoundOnCreate() {
        Long courseId = 999L;
        CreateChapterRequest request = new CreateChapterRequest();

        Chapter chapter = new Chapter();
        when(chapterMapper.toEntity(request)).thenReturn(chapter);
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> chapterService.create(courseId, request));
    }

    @Test
    void shouldReturnChapterWhenFound() {
        Long id = 10L;
        Chapter chapter = new Chapter();
        chapter.setId(id);
        chapter.setName("Intro");

        ChapterResponse expectedResponse = new ChapterResponse();
        expectedResponse.setId(id);
        expectedResponse.setName("Intro");

        when(chapterRepository.findById(id)).thenReturn(Optional.of(chapter));
        when(chapterMapper.toResponse(chapter)).thenReturn(expectedResponse);

        ChapterResponse result = chapterService.getById(id);

        assertEquals(id, result.getId());
        assertEquals("Intro", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenChapterNotFoundOnGetById() {
        Long id = 999L;
        when(chapterRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> chapterService.getById(id));
    }

    @Test
    void shouldUpdateChapter() {
        Long id = 10L;
        UpdateChapterRequest request = new UpdateChapterRequest();
        request.setName("Updated Intro");

        Chapter chapter = new Chapter();
        chapter.setId(id);
        chapter.setName("Intro");

        Chapter savedChapter = new Chapter();
        savedChapter.setId(id);
        savedChapter.setName("Updated Intro");

        ChapterResponse expectedResponse = new ChapterResponse();
        expectedResponse.setId(id);
        expectedResponse.setName("Updated Intro");

        when(chapterRepository.findById(id)).thenReturn(Optional.of(chapter));
        when(chapterRepository.save(chapter)).thenReturn(savedChapter);
        when(chapterMapper.toResponse(savedChapter)).thenReturn(expectedResponse);

        ChapterResponse result = chapterService.update(id, request);

        assertEquals("Updated Intro", result.getName());
        verify(chapterMapper).updateEntity(request, chapter);
    }

    @Test
    void shouldThrowExceptionWhenChapterNotFoundOnUpdate() {
        Long id = 999L;
        UpdateChapterRequest request = new UpdateChapterRequest();
        when(chapterRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> chapterService.update(id, request));
    }

    @Test
    void shouldDeleteChapter() {
        Long id = 10L;
        when(chapterRepository.existsById(id)).thenReturn(true);

        chapterService.delete(id);

        verify(chapterRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenChapterNotFoundOnDelete() {
        Long id = 999L;
        when(chapterRepository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> chapterService.delete(id));
        verify(chapterRepository, never()).deleteById(any());
    }
}