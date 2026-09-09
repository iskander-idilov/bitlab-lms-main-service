package kz.bitlab.springboot.mainservice.mapper;
import kz.bitlab.springboot.mainservice.dto.response.AttachmentResponse;
import kz.bitlab.springboot.mainservice.entity.Attachment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AttachmentMapper {
    @Mapping(source = "lesson.id", target = "lessonId")
    AttachmentResponse toResponse(Attachment attachment);
}
