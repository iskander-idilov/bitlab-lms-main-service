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
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChapterService {
    private final ChapterMapper chapterMapper;
    private final ChapterRepository chapterRepository;
    private final CourseRepository courseRepository;

    @Transactional
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

    @Transactional(readOnly = true)
    public ChapterResponse getById (Long courseId, Long id){
        log.debug("Fetching course with id: {}", id);

        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found with id: " + id));

        if (!chapter.getCourse().getId().equals(courseId)){
            throw new IllegalArgumentException("Chapter with id: " + id + " does not belong to course with id: " + courseId);
        }
        return chapterMapper.toResponse(chapter);
    }

    @Transactional
    public ChapterResponse update (Long courseId, Long id, UpdateChapterRequest request){
        log.info("Updating chapter with id: {}", id);
        log.debug("Request data: {}", request);

        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found with id: " + id));

        if (!chapter.getCourse().getId().equals(courseId)){
            throw new IllegalArgumentException("Chapter with id: " + id + " does not belong to course with id: " + courseId);
        }

        chapterMapper.updateEntity(request, chapter);
        Chapter saved = chapterRepository.save(chapter);


        return chapterMapper.toResponse(saved);
    }

    @Transactional
    public void delete (Long courseId, Long id){
        log.info("Deleting chapter with id: {}", id);

        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Chapter not found with id: " + id));

        if (!chapter.getCourse().getId().equals(courseId)){
            throw new IllegalArgumentException("Chapter with id: " + id + " does not belong to course with id: " + courseId);
        }

        chapterRepository.deleteById(id);
    }
}
