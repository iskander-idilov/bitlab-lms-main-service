package kz.bitlab.springboot.mainservice.service;
import kz.bitlab.springboot.mainservice.dto.request.CreateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.response.CourseResponse;
import kz.bitlab.springboot.mainservice.entity.Course;
import kz.bitlab.springboot.mainservice.mapper.CourseMapper;
import kz.bitlab.springboot.mainservice.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;

    public CourseResponse create (CreateCourseRequest request){
        log.info("Creating new course");
        log.debug("Request data: {}", request);

        Course course = courseMapper.toEntity(request);
        Course saved = courseRepository.save(course);

        return courseMapper.toResponse(saved);
    }

    public CourseResponse getById(Long id){
        log.info("Fetching course with id: {}", id);

        Course result = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));

        return courseMapper.toResponse(result);
    }

    public CourseResponse update(Long id, UpdateCourseRequest request){
        log.info("Updating course with id: {}", id);
        log.debug("Request data: {}", request);

        Course course = courseRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("Course not found with id: " + id));
        courseMapper.updateEntity(request, course);
        Course saved = courseRepository.save(course);

        return courseMapper.toResponse(saved);
    }

    public void delete (Long id){
        log.info("Deleting course with id: {}", id);

        if (!courseRepository.existsById(id)) {
            throw new IllegalArgumentException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

}
