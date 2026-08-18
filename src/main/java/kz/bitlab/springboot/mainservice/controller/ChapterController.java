package kz.bitlab.springboot.mainservice.controller;
import jakarta.validation.Valid;
import kz.bitlab.springboot.mainservice.dto.request.CreateChapterRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateChapterRequest;
import kz.bitlab.springboot.mainservice.dto.response.ChapterResponse;
import kz.bitlab.springboot.mainservice.service.ChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses/{courseId}/chapters")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService chapterService;

    @PostMapping
    public ResponseEntity <ChapterResponse> create (@PathVariable Long courseId, @Valid @RequestBody CreateChapterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(chapterService.create(courseId, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity <ChapterResponse> getById (@PathVariable Long courseId, @PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(chapterService.getById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity <ChapterResponse> update (@PathVariable Long courseId, @PathVariable Long id, @Valid @RequestBody UpdateChapterRequest request){
        return ResponseEntity.status(HttpStatus.OK).body(chapterService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity <Void> delete (@RequestParam Long id){
        chapterService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
