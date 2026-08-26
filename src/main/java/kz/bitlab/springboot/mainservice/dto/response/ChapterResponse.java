package kz.bitlab.springboot.mainservice.dto.response;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class ChapterResponse {
    private Long id;
    private String name;
    private String description;
    private Integer order;
    private Long courseId;
    private LocalDateTime createdTime;
    private LocalDateTime updatedTime;
}
