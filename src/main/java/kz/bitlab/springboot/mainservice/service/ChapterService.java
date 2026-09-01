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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChapterService {
    private final ChapterMapper chapterMapper;
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;

    public ChapterResponse create (Long courseId, CreateChapterRequest request){
        log.info("Creating new chapter");
        log.debug("Request data: {}", request);

        Chapter chapter = chapterMapper.toEntity(request);
        Course course = courseRepository.findById(courseId).
                orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + courseId));
        chapter.setCourse(course);
        Chapter saved = chapterRepository.save(chapter);

        return chapterMapper.toResponse(saved);
    }

    public ChapterResponse getById (Long id){
        log.info("Fetching chapter with id: {}", id);

        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found with id: " + id));

        return chapterMapper.toResponse(chapter);
    }

    public ChapterResponse update (Long id, UpdateChapterRequest request){
        log.info("Updating chapter with id: {}", id);
        log.debug("Request data: {}", request);

        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found with id: " + id));
        chapterMapper.updateEntity(request, chapter);
        Chapter saved = chapterRepository.save(chapter);

        return chapterMapper.toResponse(saved);
    }

    public void delete (Long id){
        log.info("Deleting chapter with id: {}", id);

        if (!chapterRepository.existsById(id)){
            throw new IllegalArgumentException("Course not found with id: " + id);
        }
        chapterRepository.deleteById(id);
    }
}
