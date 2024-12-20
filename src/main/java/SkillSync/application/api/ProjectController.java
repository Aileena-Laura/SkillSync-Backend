package SkillSync.application.api;

import SkillSync.application.dto.ProjectRequest;
import SkillSync.application.dto.ProjectResponse;
import SkillSync.application.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    private ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public Page<ProjectResponse> getAllProjects(Pageable pageable){
        return projectService.getAllProjects(pageable);
    }

    @GetMapping("/match")
    public List<ProjectResponse> getProjectsByMatch(
            @RequestParam String studentId,
            Pageable pageable) {
        return projectService.getAllProjectsByMatch(pageable, studentId);
    }

    @GetMapping("/{id}")
    public ProjectResponse getProjectById(@PathVariable int id){
        return projectService.getProjectById(id);
    }

    @GetMapping("/company/{id}")
    public List<ProjectResponse> getProjectsByCompanyId(@PathVariable String id){
        return projectService.getProjectsByCompanyId(id);
    }

    @PostMapping
    public ProjectResponse createProject(@RequestBody ProjectRequest body) {
        return projectService.createProject(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable int id){
        projectService.deleteProject(id);
        return ResponseEntity.status(HttpStatus.CREATED).body("{\"message\": \"Project deleted successfully\"}");
    }
}
