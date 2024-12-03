package SkillSync.application.dto;

import SkillSync.application.entity.Skill;
import SkillSync.application.entity.StudentProfile;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class StudentResponse {
    String username;
    String email;
    String firstName;
    String lastName;
    String description;
    String location;
    String role;
    List<SkillResponse> skills;


    public StudentResponse(StudentProfile studentProfile){
        this.username = studentProfile.getStudentId();
        this.email = studentProfile.getUserId().getEmail();
        this.firstName = studentProfile.getFirstName();
        this.lastName = studentProfile.getLastName();
        this.role = studentProfile.getUserId().getRole().toString();
        this.description = studentProfile.getDescription();
        this.location = studentProfile.getLocation();
        this.skills = studentProfile.getSkills().stream().map(SkillResponse::new).toList();
    }
}
