package kz.bitlab.springboot.mainservice.repository;
import kz.bitlab.springboot.mainservice.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository <Attachment, Long> {
}
