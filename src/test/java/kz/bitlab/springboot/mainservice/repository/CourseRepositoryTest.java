package kz.bitlab.springboot.mainservice.repository;
import kz.bitlab.springboot.mainservice.AbstractIntegrationTest;
import kz.bitlab.springboot.mainservice.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CourseRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private CourseRepository courseRepository;

    @Test
    void shouldSaveAndFindCourse(){
        Course course = new Course();
        course.setName("Test Course");
        course.setDescription("Integration test description");
        course.setCreatedTime(LocalDateTime.now());

        Course saved = courseRepository.save(course);

        assertThat(saved.getId()).isNotNull();

        Optional <Course> found = courseRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Course");
    }
}
