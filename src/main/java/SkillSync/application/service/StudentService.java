package SkillSync.application.service;

import SkillSync.application.dto.StudentResponse;
import SkillSync.application.entity.StudentProfile;
import SkillSync.application.repository.StudentProfileRepository;
import SkillSync.security.dto.UserWithRolesResponse;
import SkillSync.security.entity.UserWithRoles;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StudentService {
    private final StudentProfileRepository studentProfileRepository;

    public StudentService(StudentProfileRepository studentProfileRepository){
        this.studentProfileRepository = studentProfileRepository;
    }

    public StudentResponse getStudentProfile(String id){
        StudentProfile user = studentProfileRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
        return new StudentResponse(user);
    }
}
