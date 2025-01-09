package SkillSync.application.api;

import SkillSync.application.dto.ProjectRequest;
import SkillSync.application.dto.ProjectResponse;
import SkillSync.application.entity.Project;
import SkillSync.application.service.ProjectService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> getProjectsByMatch(
            @RequestParam String studentId,
            Pageable pageable) {
        Map<String, Object> response = projectService.getAllProjectsByMatch(pageable, studentId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Map<String, Object>> searchProjects(
            @RequestParam(required = false) String term,
            @RequestParam String userId,
            Pageable pageable) {

        Map<String, Object> searchResults = projectService.searchProjects(term, userId, pageable);

        return ResponseEntity.ok(searchResults);
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
