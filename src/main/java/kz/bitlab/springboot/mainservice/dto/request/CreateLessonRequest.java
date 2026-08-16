package kz.bitlab.springboot.mainservice.dto.request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateLessonRequest {
    @NotBlank
    @Size(min = 3, max = 255)
    private String name;

    @Size(max = 2000)
    private String description;

    @NotBlank
    @Size(max = 5000)
    private String content;

    @NotNull
    @Min(1)
    private Integer order;
}
