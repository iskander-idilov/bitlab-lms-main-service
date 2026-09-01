package kz.bitlab.springboot.mainservice.controller;
import jakarta.validation.Valid;
import kz.bitlab.springboot.mainservice.dto.request.CreateUserRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateRoleRequest;
import kz.bitlab.springboot.mainservice.dto.request.UpdateUserRequest;
import kz.bitlab.springboot.mainservice.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/me")
    public ResponseEntity<Void> updateMe(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UpdateUserRequest request) {
        userManagementService.updateUser(jwt.getSubject(), request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateRole(
            @PathVariable String id,
            @Valid @RequestBody UpdateRoleRequest request) {
        userManagementService.updateUserRole(id, request);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}