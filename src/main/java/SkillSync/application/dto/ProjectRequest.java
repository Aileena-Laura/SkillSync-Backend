package SkillSync.application.dto;

import SkillSync.application.entity.Project;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRequest {
    private String title;
    private String description;
    private String companyId;
    private List<Integer> requiredSkills;
}
