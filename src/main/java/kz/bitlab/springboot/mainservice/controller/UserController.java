package kz.bitlab.springboot.mainservice.controller;
import jakarta.validation.Valid;
import kz.bitlab.springboot.mainservice.dto.request.CreateUserRequest;
import kz.bitlab.springboot.mainservice.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserManagementService userManagementService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserRequest request) {
        userManagementService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}