package kz.bitlab.springboot.mainservice.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.bitlab.springboot.mainservice.dto.request.CreateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateCourseRequest;
import kz.bitlab.springboot.mainservice.dto.response.CourseResponse;
import kz.bitlab.springboot.mainservice.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Courses", description = "Управление курсами")
@RestController
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {
      private final CourseService courseService;

      @Operation(summary = "Создать новый курс", description = "Создаёт курс с указанным названием и описанием")
      @ApiResponses(value = {
              @ApiResponse(responseCode = "201", description = "Курс успешно создан"),
              @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
      })
      @PostMapping
      public ResponseEntity <CourseResponse> create (@Valid @RequestBody CreateCourseRequest request){
          return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(request));
      }
      @Operation(summary = "Получить курс", description = "Даёт пользователю запрашиваемый курс")
      @ApiResponses(value = {
              @ApiResponse(responseCode = "200", description = "Курс успешно найден"),
              @ApiResponse(responseCode = "404", description = "Курс не найден")
      })
      @GetMapping("/{id}")
      public ResponseEntity <CourseResponse> getById (@PathVariable Long id){
          return ResponseEntity.status(HttpStatus.OK).body(courseService.getById(id));
      }

      @Operation(summary = "Частично обновить данные", description = "Позволяет обновить данные только по полученным полям")
      @ApiResponses(value = {
              @ApiResponse(responseCode = "200", description = "Данные обновлены"),
              @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
              @ApiResponse(responseCode = "404", description = "Курс не найден")
      })
      @PatchMapping("/{id}")
      public ResponseEntity <CourseResponse> update (@PathVariable Long id, @Valid @RequestBody UpdateCourseRequest request){
          return ResponseEntity.status(HttpStatus.OK).body(courseService.update(id, request));
      }
      @Operation(summary = "Удалить курс", description = "Удаляет курс по id")
      @ApiResponses(value = {
              @ApiResponse(responseCode = "204", description = "Курс успешно удален"),
              @ApiResponse(responseCode = "404", description = "Курс не найден")
      })
      @DeleteMapping("/{id}")
      public ResponseEntity <Void> delete (@PathVariable Long id){
          courseService.delete(id);
          return ResponseEntity.noContent().build();
      }
}
