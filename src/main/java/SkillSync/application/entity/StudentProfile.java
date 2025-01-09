package SkillSync.application.entity;

import SkillSync.security.entity.UserWithRoles;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class StudentProfile {
    @Id
    @Column(name = "student_id")
    private String studentId;

    private String firstName;
    private String lastName;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String location;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_account_id")
    @MapsId
    private UserWithRoles userId;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "student_skill",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> skills = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "current_education_id", referencedColumnName = "id")
    private Education currentEducation;

    public StudentProfile(UserWithRoles user, String firstName, String lastName){
        this.userId = user;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addSkill(Skill skill){
        this.skills.add(skill);
    }

    public void removeSkill(Skill skill) {
        this.skills.remove(skill);
    }
}
