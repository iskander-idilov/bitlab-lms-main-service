package kz.bitlab.springboot.mainservice.mapper;
import kz.bitlab.springboot.mainservice.dto.request.CreateLessonRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateLessonRequest;
import kz.bitlab.springboot.mainservice.dto.response.LessonResponse;
import kz.bitlab.springboot.mainservice.entity.Lesson;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LessonMapper {
    Lesson toEntity(CreateLessonRequest request);
    @Mapping(source = "chapter.id", target = "chapterId")
    LessonResponse toResponse(Lesson lesson);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateLessonRequest request, @MappingTarget Lesson lesson);
}
