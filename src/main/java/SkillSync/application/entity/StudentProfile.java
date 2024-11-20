package SkillSync.application.entity;

import SkillSync.security.entity.UserWithRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class StudentProfile {
    @Id
    private String accountId;

    private String firstName;
    private String lastName;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private UserWithRoles userId;

    public StudentProfile(UserWithRoles user, String firstName, String lastName){
        this.userId = user;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
