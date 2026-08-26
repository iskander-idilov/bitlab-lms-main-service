package kz.bitlab.springboot.mainservice.repository;
import kz.bitlab.springboot.mainservice.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository <Course, Long> {
}
