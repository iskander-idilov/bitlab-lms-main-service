package kz.bitlab.springboot.mainservice.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kz.bitlab.springboot.mainservice.dto.request.CreateChapterRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateChapterRequest;
import kz.bitlab.springboot.mainservice.dto.response.ChapterResponse;
import kz.bitlab.springboot.mainservice.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Chapters", description = "Управление главами")
@RestController
@RequestMapping("/courses/{courseId}/chapters")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @Operation(summary = "Создать новую главу", description = "Создаёт главу с указанным названием и описанием")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Глава успешно создана"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Глава с указанным courseId не найдена")
    })
    @PostMapping
    public ResponseEntity <ChapterResponse> create (@PathVariable Long courseId, @Valid @RequestBody CreateChapterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(chapterService.create(courseId, request));
    }
    @Operation(summary = "Получить главу", description = "Даёт пользователю запрашиваемую главу")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Глава успешно найдена"),
            @ApiResponse(responseCode = "404", description = "Глава не найдена")
    })
    @GetMapping("/{id}")
    public ResponseEntity <ChapterResponse> getById (@PathVariable Long courseId, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(chapterService.getById(id));
    }
    @Operation(summary = "Частично обновить данные", description = "Позволяет обновить данные только по полученным полям")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Данные обновлены"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Глава не найдена")
    })
    @PatchMapping("/{id}")
    public ResponseEntity <ChapterResponse> update (@PathVariable Long courseId, @PathVariable Long id, @Valid @RequestBody UpdateChapterRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(chapterService.update(id, request));
    }
    @Operation(summary = "Удалить главу", description = "Удаляет главу по id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Глава успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Глава не найдена")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete (@PathVariable Long courseId, @PathVariable Long id){
        chapterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
