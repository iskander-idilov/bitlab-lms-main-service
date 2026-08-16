package kz.bitlab.springboot.mainservice.service;
import kz.bitlab.springboot.mainservice.dto.request.CreateChapterRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateChapterRequest;
import kz.bitlab.springboot.mainservice.dto.response.ChapterResponse;
import kz.bitlab.springboot.mainservice.entity.Chapter;
import kz.bitlab.springboot.mainservice.entity.Course;
import kz.bitlab.springboot.mainservice.mapper.ChapterMapper;
import kz.bitlab.springboot.mainservice.repository.ChapterRepository;
import kz.bitlab.springboot.mainservice.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChapterService {
    private final ChapterMapper chapterMapper;
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;

    public ChapterResponse create(Long courseId, CreateChapterRequest request){
        Chapter chapter = chapterMapper.toEntity(request);
        chapter.setCreatedTime(LocalDateTime.now());
        Course course = courseRepository.findById(courseId).
                orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));
        chapter.setCourse(course);
        Chapter saved = chapterRepository.save(chapter);

        return chapterMapper.toResponse(saved);
    }

    public ChapterResponse getById(Long id){
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found with id: " + id));

        return chapterMapper.toResponse(chapter);
    }

    public ChapterResponse update(Long id, UpdateChapterRequest request){
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found with id: " + id));
        chapterMapper.updateEntity(request, chapter);
        chapter.setUpdatedTime(LocalDateTime.now());
        Chapter saved = chapterRepository.save(chapter);

        return chapterMapper.toResponse(saved);
    }
}
