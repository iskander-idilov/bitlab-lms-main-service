package kz.bitlab.springboot.mainservice.dto.request;
import jakarta.validation.constraints.Email;

public record UpdateUserRequest(
        String firstName,
        String lastName,
        @Email String email,
        String password
) {
}