package kz.bitlab.springboot.mainservice.controller;
import jakarta.validation.Valid;
import kz.bitlab.springboot.mainservice.dto.request.LoginRequest;
import kz.bitlab.springboot.mainservice.dto.response.TokenResponse;
import kz.bitlab.springboot.mainservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(request));
    }
}