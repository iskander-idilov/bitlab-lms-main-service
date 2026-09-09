package kz.bitlab.springboot.mainservice.service;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import kz.bitlab.springboot.mainservice.config.MinioProperties;
import kz.bitlab.springboot.mainservice.dto.response.AttachmentResponse;
import kz.bitlab.springboot.mainservice.entity.Attachment;
import kz.bitlab.springboot.mainservice.entity.Lesson;
import kz.bitlab.springboot.mainservice.mapper.AttachmentMapper;
import kz.bitlab.springboot.mainservice.repository.AttachmentRepository;
import kz.bitlab.springboot.mainservice.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;
import io.minio.GetObjectArgs;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttachmentService {
     private final MinioClient minioClient;
     private final MinioProperties minioProperties;
     private final AttachmentRepository attachmentRepository;
     private final LessonRepository lessonRepository;
     private final AttachmentMapper attachmentMapper;

     public AttachmentResponse upload(Long lessonId, MultipartFile file){
         log.info("Uploading attachment for lesson: {}", lessonId);

         Lesson lesson = lessonRepository.findById(lessonId)
                 .orElseThrow(() -> new IllegalArgumentException("Lesson not found with id: " + lessonId));

         String objectName = UUID.randomUUID() + "-" + file.getOriginalFilename();

         try {
             minioClient.putObject(
                     PutObjectArgs.builder()
                             .bucket(minioProperties.bucketName())
                             .object(objectName)
                             .stream(file.getInputStream(), file.getSize(), -1L)
                             .contentType(file.getContentType())
                             .build()
             );
         } catch (Exception e) {
             log.error("Failed to upload file to MinIO", e);
             throw new RuntimeException("Failed to upload file", e);
         }

         Attachment attachment = new Attachment();
         attachment.setName(file.getOriginalFilename());
         attachment.setUrl(objectName);
         attachment.setLesson(lesson);

         Attachment saved = attachmentRepository.save(attachment);

         return attachmentMapper.toResponse(saved);
     }

    public InputStream download(Attachment attachment) {
        log.info("Downloading attachment: {}", attachment.getId());

        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(minioProperties.bucketName())
                            .object(attachment.getUrl())
                            .build()
            );
        } catch (Exception e) {
            log.error("Failed to download file from MinIO", e);
            throw new RuntimeException("Failed to download file", e);
        }
    }

    public Attachment getById(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Attachment not found with id: " + id));
    }
}
