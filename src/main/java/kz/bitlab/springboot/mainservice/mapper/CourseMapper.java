package kz.bitlab.springboot.mainservice.mapper;
import kz.bitlab.springboot.mainservice.dto.request.CreateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.response.CourseResponse;
import kz.bitlab.springboot.mainservice.entity.Course;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toEntity(CreateCourseRequest request);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "chapter", ignore = true)
    @Mapping(target = "createdTime", ignore = true)
    @Mapping(target = "updatedTime", ignore = true)
    CourseResponse toResponse (Course course);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateCourseRequest request, @MappingTarget Course course);

}
