package kz.bitlab.springboot.mainservice.service;
import kz.bitlab.springboot.mainservice.dto.request.CreateLessonRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateLessonRequest;
import kz.bitlab.springboot.mainservice.dto.response.LessonResponse;
import kz.bitlab.springboot.mainservice.entity.Chapter;
import kz.bitlab.springboot.mainservice.entity.Lesson;
import kz.bitlab.springboot.mainservice.mapper.LessonMapper;
import kz.bitlab.springboot.mainservice.repository.ChapterRepository;
import kz.bitlab.springboot.mainservice.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonMapper lessonMapper;
    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;

    public LessonResponse create (Long chapterId, CreateLessonRequest request){
        log.info("Creating new lesson");
        log.debug("Request data: {}", request);

        Lesson lesson = lessonMapper.toEntity(request);
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found with id: " + chapterId));
        lesson.setChapter(chapter);
        Lesson saved = lessonRepository.save(lesson);

        return lessonMapper.toResponse(saved);
    }

    public LessonResponse getById(Long id){
        log.info("Fetching lesson with id: {}", id);

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + id));

        return lessonMapper.toResponse(lesson);
    }

    public LessonResponse update (Long id, UpdateLessonRequest request){
        log.info("Updating lesson with id: {}", id);
        log.debug("Request data: {}", request);

        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + id));
        lessonMapper.updateEntity(request, lesson);
        Lesson saved = lessonRepository.save(lesson);

        return lessonMapper.toResponse(saved);
    }

    public void delete (Long id){
        log.info("Deleting lesson with id: {}", id);

        if (!lessonRepository.existsById(id)){
            throw new IllegalArgumentException("Lesson not found with id: " + id);
        }
        lessonRepository.deleteById(id);
    }
}
