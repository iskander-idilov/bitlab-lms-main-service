package kz.bitlab.springboot.mainservice.controller;
import jakarta.validation.Valid;
import kz.bitlab.springboot.mainservice.dto.request.CreateLessonRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateLessonRequest;
import kz.bitlab.springboot.mainservice.dto.response.LessonResponse;
import kz.bitlab.springboot.mainservice.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chapters/{chapterId}/lessons")
@RequiredArgsConstructor
public class LessonController {
    private final LessonService lessonService;

    @PostMapping
    public ResponseEntity <LessonResponse> create (@PathVariable Long chapterId, @Valid @RequestBody CreateLessonRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(lessonService.create(chapterId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity <LessonResponse> getById (@PathVariable Long chapterId, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity <LessonResponse> update (@PathVariable Long chapterId, @PathVariable Long id, @Valid @RequestBody UpdateLessonRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(lessonService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete (@RequestParam Long id){
        lessonService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
