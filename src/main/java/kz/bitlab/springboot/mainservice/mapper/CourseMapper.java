package kz.bitlab.springboot.mainservice.mapper;
import kz.bitlab.springboot.mainservice.dto.request.CreateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.response.CourseResponse;
import kz.bitlab.springboot.mainservice.entity.Course;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface CourseMapper {
    Course toEntity(CreateCourseRequest request);
    CourseResponse toResponse (Course course);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(UpdateCourseRequest request, @MappingTarget Course course);

}
