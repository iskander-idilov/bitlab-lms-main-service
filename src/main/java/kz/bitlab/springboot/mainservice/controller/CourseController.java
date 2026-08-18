package kz.bitlab.springboot.mainservice.controller;
import jakarta.validation.Valid;
import kz.bitlab.springboot.mainservice.dto.request.CreateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.response.CourseResponse;
import kz.bitlab.springboot.mainservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
      private final CourseService courseService;

      @PostMapping
      public ResponseEntity <CourseResponse> create (@Valid @RequestBody CreateCourseRequest request){
          return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(request));
      }

      @GetMapping("/{id}")
      public ResponseEntity <CourseResponse> getById (@PathVariable Long id){
          return ResponseEntity.status(HttpStatus.OK).body(courseService.getById(id));
      }

      @PatchMapping("/{id}")
      public ResponseEntity <CourseResponse> update (@PathVariable Long id, @Valid @RequestBody UpdateCourseRequest request){
          return ResponseEntity.status(HttpStatus.OK).body(courseService.update(id, request));
      }

      @DeleteMapping("/{id}")
      public ResponseEntity <Void> delete (@PathVariable Long id){
          courseService.delete(id);
          return ResponseEntity.noContent().build();
      }
}
