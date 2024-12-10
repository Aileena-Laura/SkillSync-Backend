package SkillSync.application.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SkillAPI {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int skillId;

    private String skillName;

    public SkillAPI(String skillName) {
        this.skillName = skillName;
    }
}
