package SkillSync.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentSecurityRequest {
    @NotBlank(message="Username is not provided")
    String username;
    @NotBlank(message="Password is not provided")
    String password;
    @NotBlank(message="Email is not provided")
    String email;
    @NotBlank(message="Role is not provided")
    String role;
    String firstName;
    String lastName;
}
