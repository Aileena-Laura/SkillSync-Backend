package SkillSync.application.api;

import SkillSync.application.dto.SkillAPIResponse;
import SkillSync.application.service.SkillAPIService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/skillAPI")
public class SkillAPIController {

    private final SkillAPIService service;

    public SkillAPIController(SkillAPIService service) {
        this.service = service;
    }

    @GetMapping
    public List<SkillAPIResponse> getSkills(){
        return service.getAllSkills();
    }
}
