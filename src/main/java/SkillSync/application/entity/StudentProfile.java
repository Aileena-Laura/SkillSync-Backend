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
    private String description;
    private String location;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_account_id")
    @MapsId
    private UserWithRoles userId;

    @OneToMany(mappedBy = "studentProfile", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<Skill> skills;

    public StudentProfile(UserWithRoles user, String firstName, String lastName){
        this.userId = user;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public void addSkill(Skill skill){
        if(skills == null){
            skills = new ArrayList<>();
        }
        skills.add(skill);
    }
}
