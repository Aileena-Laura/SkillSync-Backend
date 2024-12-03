package SkillSync.application.service;

import SkillSync.application.dto.StudentRequest;
import SkillSync.application.dto.StudentResponse;
import SkillSync.application.entity.StudentProfile;
import SkillSync.application.repository.SkillRepository;
import SkillSync.application.repository.StudentProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StudentService {
    private final StudentProfileRepository studentProfileRepository;
    private final SkillRepository skillRepository;
    private final SkillService skillService;

    public StudentService(StudentProfileRepository studentProfileRepository, SkillRepository skillRepository, SkillService skillService) {
        this.studentProfileRepository = studentProfileRepository;
        this.skillRepository = skillRepository;
        this.skillService = skillService;
    }

    public StudentResponse getStudentProfile(String id){
        StudentProfile user = studentProfileRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
        System.out.print(user.getSkills());
        return new StudentResponse(user);
    }

    public StudentResponse editStudentDescription(String id, StudentRequest body){
        StudentProfile student = studentProfileRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No student with this id found"));

        student.setDescription(body.getDescription());
        return new StudentResponse(studentProfileRepository.save(student));
    }

    public StudentResponse editStudentBasicInfo(String id, StudentRequest body){
        StudentProfile student = studentProfileRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No student with this id found"));
        student.setFirstName(body.getFirstName());
        student.setLastName(body.getLastName());
        student.setLocation(body.getLocation());
        student.getUserId().setEmail(body.getEmail());

        return new StudentResponse(studentProfileRepository.save(student));
    }
}
