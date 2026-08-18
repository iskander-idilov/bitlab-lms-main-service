package kz.bitlab.springboot.mainservice.dto.request;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateCourseRequest {
    @Size(min = 3, max = 255)
    private String name;

    @Size(max = 2000)
    private String description;
}
