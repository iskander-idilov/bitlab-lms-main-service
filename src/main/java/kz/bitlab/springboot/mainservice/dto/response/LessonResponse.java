package kz.bitlab.springboot.mainservice.dto.response;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class LessonResponse {
    private Long id;
    private String name;
    private String description;
    private String content;
    private Integer order;
    private Long chapterId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
