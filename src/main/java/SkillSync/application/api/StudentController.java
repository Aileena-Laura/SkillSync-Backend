package SkillSync.application.api;

import SkillSync.application.dto.StudentResponse;
import SkillSync.application.service.StudentService;
import SkillSync.security.dto.UserWithRolesResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    StudentService studentService;

    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }

    @GetMapping("/{username}")
    public StudentResponse getStudentProfileByUsername(@PathVariable String username){
        return studentService.getStudentProfile(username);
    }
}
