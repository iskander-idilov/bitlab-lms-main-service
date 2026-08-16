package kz.bitlab.springboot.mainservice.mapper;
import kz.bitlab.springboot.mainservice.dto.request.CreateChapterRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateChapterRequest;
import kz.bitlab.springboot.mainservice.dto.response.ChapterResponse;
import kz.bitlab.springboot.mainservice.entity.Chapter;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ChapterMapper {
    @Mapping(source = "course.id", target = "courseId")
    ChapterResponse toResponse(Chapter chapter);

    Chapter toEntity(CreateChapterRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateChapterRequest request, @MappingTarget Chapter chapter);
}
