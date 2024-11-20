package SkillSync.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyRequest {
    @NotBlank(message="Username is not provided")
    String username;
    @NotBlank(message="Password is not provided")
    String password;
    @NotBlank(message="Email is not provided")
    String email;
    @NotBlank(message="Role is not provided")
    String role;
    String companyName;
    String website;
    String location;
}
