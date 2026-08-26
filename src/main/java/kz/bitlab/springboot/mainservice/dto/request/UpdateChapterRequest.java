package kz.bitlab.springboot.mainservice.dto.request;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UpdateChapterRequest {
    @Size(min = 3, max = 255)
    private String name;

    @Size(max = 2000)
    private String description;

    @Min(1)
    private Integer order;
}
