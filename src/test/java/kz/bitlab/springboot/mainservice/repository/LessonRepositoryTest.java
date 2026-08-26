package kz.bitlab.springboot.mainservice.repository;
import kz.bitlab.springboot.mainservice.AbstractIntegrationTest;
import kz.bitlab.springboot.mainservice.entity.Chapter;
import kz.bitlab.springboot.mainservice.entity.Course;
import kz.bitlab.springboot.mainservice.entity.Lesson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class LessonRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    @Test
    void shouldSaveAndFindLesson(){
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
        Chapter savedChapter = chapterRepository.save(chapter);

        Lesson lesson = new Lesson();
        lesson.setName("Test Lesson");
        lesson.setDescription("Integration test description");
        lesson.setCreatedTime(LocalDateTime.now());
        lesson.setChapter(savedChapter);

        Lesson saved = lessonRepository.save(lesson);

        assertThat(saved.getId()).isNotNull();

        Optional <Lesson> found = lessonRepository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Test Lesson");
    }

}
