package SkillSync.application.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int skillId;

    private String skillName;

    @Enumerated(EnumType.STRING)
    @Column(name="experience", columnDefinition = "ENUM('LOW','MEDIUM','HIGH')")
    private SkillExperience experience;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @JsonIgnore
    private StudentProfile studentProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @JsonIgnore
    private Project project;
    public Skill(String skillName, SkillExperience experience) {
        this.skillName = skillName;
        this.experience = experience;
    }

    public void addStudent(StudentProfile student){
        this.studentProfile = student;
        student.addSkill(this);
    }

    public void addProject(Project project){
        this.project = project;
        project.addRequiredSkill(this);
    }
}
