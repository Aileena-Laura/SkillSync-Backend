package SkillSync.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequest {
    private String firstName;
    private String lastName;
    private String description;
    private String location;
    private String email;
}
