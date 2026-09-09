package kz.bitlab.springboot.mainservice.dto.response;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class AttachmentResponse {
    private Long id;
    private String name;
    private String url;
    private Long lessonId;
    private LocalDateTime createdTime;

}
