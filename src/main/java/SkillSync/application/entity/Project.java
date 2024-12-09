package SkillSync.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int projectId;
    private String title;
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Skill> requiredSkills = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private CompanyProfile companyProfile;

    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    public Project(String title, String description, CompanyProfile companyProfile) {
        this.title = title;
        this.description = description;
        this.companyProfile = companyProfile;
        companyProfile.addProject(this);
    }

    public void addRequiredSkill(Skill skill){
        requiredSkills.add(skill);
        skill.setProject(this);
    }

    public void removeRequiredSkill(Skill skill){
        requiredSkills.remove(skill);
        skill.setProject(null);
    }

}
