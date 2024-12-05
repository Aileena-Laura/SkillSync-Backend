package SkillSync.application.api;

import SkillSync.application.dto.StudentRequest;
import SkillSync.application.dto.StudentResponse;
import SkillSync.application.service.StudentService;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/description/{id}")
    public StudentResponse editStudentDescription(@PathVariable String id, @RequestBody StudentRequest body){
        return studentService.editStudentDescription(id, body);
    }

    @PatchMapping("/{id}")
    public StudentResponse editBasicInfo(@PathVariable String id, @RequestBody StudentRequest body){
        return studentService.editStudentBasicInfo(id, body);
    }
}

// Very important comment :)
