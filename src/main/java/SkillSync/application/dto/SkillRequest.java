package SkillSync.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillRequest {
    private String skillName;
    private String studentId;
    private int projectId;
}
