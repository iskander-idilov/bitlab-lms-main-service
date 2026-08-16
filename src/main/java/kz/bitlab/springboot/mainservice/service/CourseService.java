package kz.bitlab.springboot.mainservice.service;
import kz.bitlab.springboot.mainservice.dto.request.CreateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.response.CourseResponse;
import kz.bitlab.springboot.mainservice.entity.Course;
import kz.bitlab.springboot.mainservice.mapper.CourseMapper;
import kz.bitlab.springboot.mainservice.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;

    public CourseResponse create (CreateCourseRequest request){
        Course course = courseMapper.toEntity(request);
        course.setCreatedTime(LocalDateTime.now());
        Course saved = courseRepository.save(course);

        return courseMapper.toResponse(saved);
    }

    public CourseResponse getById(Long id){
        Course result = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));

        return courseMapper.toResponse(result);
    }

    public CourseResponse update(Long id, UpdateCourseRequest request){
        Course course = courseRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        courseMapper.updateEntity(request, course);
        course.setUpdatedTime(LocalDateTime.now());
        Course saved = courseRepository.save(course);

        return courseMapper.toResponse(saved);
    }

}
