package kz.bitlab.springboot.mainservice.dto.request;
import jakarta.validation.constraints.NotNull;

public record UpdateRoleRequest(
        @NotNull UserRole role
) {
}