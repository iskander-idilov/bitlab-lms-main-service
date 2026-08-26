package kz.bitlab.springboot.mainservice.repository;
import kz.bitlab.springboot.mainservice.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonRepository extends JpaRepository <Lesson, Long> {
}
