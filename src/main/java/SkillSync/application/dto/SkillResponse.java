package SkillSync.application.dto;

import SkillSync.application.entity.Skill;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SkillResponse {
    private int id;
    private String skillName;

    public SkillResponse(Skill skill){
        this.id = skill.getSkillId();
        this.skillName = skill.getSkillName();
    }
}
