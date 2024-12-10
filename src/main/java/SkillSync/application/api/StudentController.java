package SkillSync.application.api;

import SkillSync.application.dto.SkillResponse;
import SkillSync.application.dto.StudentRequest;
import SkillSync.application.dto.StudentResponse;
import SkillSync.application.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PatchMapping("{studentId}/skill/{skillId}")
    public SkillResponse addSkillToStudentProfile(@PathVariable String studentId, @PathVariable int skillId){
        return studentService.addSkillToStudentProfile(studentId, skillId);
    }

    @PatchMapping("/description/{id}")
    public StudentResponse editStudentDescription(@PathVariable String id, @RequestBody StudentRequest body){
        return studentService.editStudentDescription(id, body);
    }

    @PatchMapping("/{id}")
    public StudentResponse editBasicInfo(@PathVariable String id, @RequestBody StudentRequest body){
        return studentService.editStudentBasicInfo(id, body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudentAccount(@PathVariable String id){
        studentService.deleteStudentAccount(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Account deleted successfully\"}");
    }

    @DeleteMapping("{studentId}/skill/{skillId}")
    public ResponseEntity<String> deleteSkillFromStudent(@PathVariable String studentId, @PathVariable int skillId){
        studentService.removeSkillFromStudent(studentId, skillId);
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Skill deleted successfully\"}");
    }

}

// Very important comment :)
