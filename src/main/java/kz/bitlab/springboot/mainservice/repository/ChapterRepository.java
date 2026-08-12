package kz.bitlab.springboot.mainservice.repository;
import kz.bitlab.springboot.mainservice.entity.Chapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository <Chapter, Long> {
}
