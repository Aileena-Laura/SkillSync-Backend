package SkillSync.application.dto;

import SkillSync.application.entity.StudentProfile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentResponse {
    String username;
    String email;
    String firstName;
    String lastName;

    public StudentResponse(StudentProfile studentProfile){
        this.username = studentProfile.getAccountId();
        this.email = studentProfile.getUserId().getEmail();
        this.firstName = studentProfile.getFirstName();
        this.lastName = studentProfile.getLastName();
    }
}
