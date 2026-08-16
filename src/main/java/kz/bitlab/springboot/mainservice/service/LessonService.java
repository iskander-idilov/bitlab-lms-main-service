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
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonMapper lessonMapper;
    private final LessonRepository lessonRepository;
    private final ChapterRepository chapterRepository;

    public LessonResponse create (Long chapterId, CreateLessonRequest request){
        Lesson lesson = lessonMapper.toEntity(request);
        lesson.setCreatedTime(LocalDateTime.now());
        Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new RuntimeException("Chapter not found with id: " + chapterId));
        lesson.setChapter(chapter);
        Lesson saved = lessonRepository.save(lesson);

        return lessonMapper.toResponse(saved);
    }

    public LessonResponse getById(Long id){
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + id));

        return lessonMapper.toResponse(lesson);
    }

    public LessonResponse update (Long id, UpdateLessonRequest request){
        Lesson lesson = lessonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lesson not found with id: " + id));
        lessonMapper.updateEntity(request, lesson);
        lesson.setUpdatedTime(LocalDateTime.now());
        Lesson saved = lessonRepository.save(lesson);

        return lessonMapper.toResponse(saved);
    }
}
