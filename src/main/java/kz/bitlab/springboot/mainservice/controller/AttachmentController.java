package kz.bitlab.springboot.mainservice.controller;
import kz.bitlab.springboot.mainservice.entity.Attachment;
import kz.bitlab.springboot.mainservice.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import java.io.InputStream;
import kz.bitlab.springboot.mainservice.dto.response.AttachmentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;
    @GetMapping("/download/{id}")
    public ResponseEntity<InputStreamResource> download(@PathVariable Long id) {
        Attachment attachment = attachmentService.getById(id);
        InputStream fileStream = attachmentService.download(attachment);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachment.getName() + "\"")
                .body(new InputStreamResource(fileStream));
    }

    @PostMapping("/upload")
    public ResponseEntity<AttachmentResponse> upload(
            @RequestParam("lessonId") Long lessonId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED).body(attachmentService.upload(lessonId, file));
    }
}
