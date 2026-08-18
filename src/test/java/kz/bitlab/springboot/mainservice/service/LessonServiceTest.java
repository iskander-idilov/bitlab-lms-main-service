package kz.bitlab.springboot.mainservice.service;
import kz.bitlab.springboot.mainservice.dto.request.CreateLessonRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateLessonRequest;
import kz.bitlab.springboot.mainservice.dto.response.LessonResponse;
import kz.bitlab.springboot.mainservice.entity.Chapter;
import kz.bitlab.springboot.mainservice.entity.Lesson;
import kz.bitlab.springboot.mainservice.mapper.LessonMapper;
import kz.bitlab.springboot.mainservice.repository.ChapterRepository;
import kz.bitlab.springboot.mainservice.repository.LessonRepository;
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
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;

    @Mock
    private ChapterRepository chapterRepository;

    @Mock
    private LessonMapper lessonMapper;

    @InjectMocks
    private LessonService lessonService;

    @Test
    void shouldCreateLesson() {
        Long chapterId = 1L;
        CreateLessonRequest request = new CreateLessonRequest();
        request.setName("Variables");
        request.setContent("Lesson content");
        request.setOrder(1);

        Chapter chapter = new Chapter();
        chapter.setId(chapterId);

        Lesson lesson = new Lesson();
        lesson.setName("Variables");

        Lesson savedLesson = new Lesson();
        savedLesson.setId(100L);
        savedLesson.setName("Variables");

        LessonResponse expectedResponse = new LessonResponse();
        expectedResponse.setId(100L);
        expectedResponse.setName("Variables");

        when(lessonMapper.toEntity(request)).thenReturn(lesson);
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.of(chapter));
        when(lessonRepository.save(lesson)).thenReturn(savedLesson);
        when(lessonMapper.toResponse(savedLesson)).thenReturn(expectedResponse);

        LessonResponse result = lessonService.create(chapterId, request);

        assertEquals(100L, result.getId());
        assertEquals("Variables", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenChapterNotFoundOnCreate() {
        Long chapterId = 999L;
        CreateLessonRequest request = new CreateLessonRequest();

        Lesson lesson = new Lesson();
        when(lessonMapper.toEntity(request)).thenReturn(lesson);
        when(chapterRepository.findById(chapterId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> lessonService.create(chapterId, request));
    }

    @Test
    void shouldReturnLessonWhenFound() {
        Long id = 100L;
        Lesson lesson = new Lesson();
        lesson.setId(id);
        lesson.setName("Variables");

        LessonResponse expectedResponse = new LessonResponse();
        expectedResponse.setId(id);
        expectedResponse.setName("Variables");

        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));
        when(lessonMapper.toResponse(lesson)).thenReturn(expectedResponse);

        LessonResponse result = lessonService.getById(id);

        assertEquals(id, result.getId());
        assertEquals("Variables", result.getName());
    }

    @Test
    void shouldThrowExceptionWhenLessonNotFoundOnGetById() {
        Long id = 999L;
        when(lessonRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> lessonService.getById(id));
    }

    @Test
    void shouldUpdateLesson() {
        Long id = 100L;
        UpdateLessonRequest request = new UpdateLessonRequest();
        request.setContent("Updated content");

        Lesson lesson = new Lesson();
        lesson.setId(id);
        lesson.setName("Variables");
        lesson.setContent("Old content");

        Lesson savedLesson = new Lesson();
        savedLesson.setId(id);
        savedLesson.setName("Variables");
        savedLesson.setContent("Updated content");

        LessonResponse expectedResponse = new LessonResponse();
        expectedResponse.setId(id);
        expectedResponse.setContent("Updated content");

        when(lessonRepository.findById(id)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(lesson)).thenReturn(savedLesson);
        when(lessonMapper.toResponse(savedLesson)).thenReturn(expectedResponse);

        LessonResponse result = lessonService.update(id, request);

        assertEquals("Updated content", result.getContent());
        verify(lessonMapper).updateEntity(request, lesson);
    }

    @Test
    void shouldThrowExceptionWhenLessonNotFoundOnUpdate() {
        Long id = 999L;
        UpdateLessonRequest request = new UpdateLessonRequest();
        when(lessonRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> lessonService.update(id, request));
    }

    @Test
    void shouldDeleteLesson() {
        Long id = 100L;
        when(lessonRepository.existsById(id)).thenReturn(true);

        lessonService.delete(id);

        verify(lessonRepository).deleteById(id);
    }

    @Test
    void shouldThrowExceptionWhenLessonNotFoundOnDelete() {
        Long id = 999L;
        when(lessonRepository.existsById(id)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> lessonService.delete(id));
        verify(lessonRepository, never()).deleteById(any());
    }
}