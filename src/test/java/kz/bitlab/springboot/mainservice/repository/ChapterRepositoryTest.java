package kz.bitlab.springboot.mainservice.repository;
import kz.bitlab.springboot.mainservice.AbstractIntegrationTest;
import kz.bitlab.springboot.mainservice.entity.Chapter;
import kz.bitlab.springboot.mainservice.entity.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDateTime;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

public class ChapterRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void shouldSaveAndFindChapter(){
        Course course = new Course();
        course.setName("Test Course");
        course.setDescription("Integration test description");
        course.setCreatedTime(LocalDateTime.now());
        Course savedCourse = courseRepository.save(course);

        Chapter chapter = new Chapter();
        chapter.setName("Test Chapter");
        chapter.setDescription("Integration test description");
        chapter.setCreatedTime(LocalDateTime.now());
        chapter.setCourse(savedCourse);

        Chapter saved = chapterRepository.save(chapter);

        assertThat(saved.getId()).isNotNull();

        Optional <Chapter> found = chapterRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Chapter");
    }
}
