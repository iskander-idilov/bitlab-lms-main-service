package kz.bitlab.springboot.mainservice.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CreateCourseRequest {
    @NotBlank
    @Size(min = 3, max = 255)
    private String name;

    @Size(max = 2000)
    private String description;
}
