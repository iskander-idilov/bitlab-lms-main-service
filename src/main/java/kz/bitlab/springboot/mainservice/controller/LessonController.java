package kz.bitlab.springboot.mainservice.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.bitlab.springboot.mainservice.dto.request.CreateLessonRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateLessonRequest;
import kz.bitlab.springboot.mainservice.dto.response.LessonResponse;
import kz.bitlab.springboot.mainservice.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Lessons", description = "Управление уроками")
@RestController
@RequestMapping("/chapters/{chapterId}/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @Operation(summary = "Создать новый урок", description = "Создаёт урок с указанным названием и описанием")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Урок успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "родительская глава не найдена")
    })
    @PostMapping
    public ResponseEntity <LessonResponse> create (@PathVariable Long chapterId, @Valid @RequestBody CreateLessonRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.create(chapterId, request));
    }

    @Operation(summary = "Получить конкретный урок", description = "Даёт пользователю запрашиваемый урок")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Урок успешно найден"),
            @ApiResponse(responseCode = "404", description = "Урок не найден")
    })
    @GetMapping("/{id}")
    public ResponseEntity <LessonResponse> getById (@PathVariable Long chapterId, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.getById(id));
    }
    @Operation(summary = "Частично обновить данные", description = "Позволяет обновить данные только по полученным полям")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные обновлены"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Урок не найден")
    })
    @PatchMapping("/{id}")
    public ResponseEntity <LessonResponse> update (@PathVariable Long chapterId, @PathVariable Long id, @Valid @RequestBody UpdateLessonRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.update(id, request));
    }
    @Operation(summary = "Удалить урок", description = "Удаляет урок по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Урок успешно удален"),
            @ApiResponse(responseCode = "404", description = "Урок не найден")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete (@PathVariable Long chapterId, @PathVariable Long id){
        lessonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
