package SkillSync.application.api;

import SkillSync.application.dto.SkillRequest;
import SkillSync.application.dto.SkillResponse;
import SkillSync.application.service.SkillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/skill")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public SkillResponse addSkillToStudent(@RequestBody SkillRequest skillRequest) {
        return skillService.addSkillToStudentProfile(skillRequest);
    }

    @PostMapping("/project")
    public SkillResponse addSkillToProject(@RequestBody SkillRequest skillRequest){
        return skillService.addSkillToProject(skillRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSkill(@PathVariable int id){
        skillService.deleteSkillById(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Skill deleted successfully\"}");
    }
}
