package SkillSync.application.dto;

import SkillSync.application.entity.SkillAPI;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillAPIResponse {
    private String skillName;

    public SkillAPIResponse(SkillAPI skill) {
        this.skillName = skill.getSkillName();
    }
}
