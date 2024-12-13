package SkillSync.application.service;

import SkillSync.application.dto.ProjectRequest;
import SkillSync.application.dto.ProjectResponse;
import SkillSync.application.dto.SkillRequest;
import SkillSync.application.entity.*;
import SkillSync.application.repository.CompanyProfileRepository;
import SkillSync.application.repository.ProjectRepository;
import SkillSync.application.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CompanyProfileRepository companyRepository;
    private final SkillRepository skillRepository;

    public ProjectService(ProjectRepository projectRepository, CompanyProfileRepository companyRepository, SkillRepository skillRepository) {
        this.projectRepository = projectRepository;
        this.companyRepository = companyRepository;
        this.skillRepository = skillRepository;
    }

    public Page<ProjectResponse> getAllProjects(Pageable pageable){
        Page<Project> projects = projectRepository.findAll(pageable);
        return projects.map(project -> new ProjectResponse(project));
    }

    public ProjectResponse getProjectById(int id){
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No project with this id found"));
        return new ProjectResponse(project);
    }

    public List<ProjectResponse> getProjectsByCompanyId(String id){
        List<Project> projects = projectRepository.findByCompanyProfile_AccountId(id);
        return projects.stream().map(project -> new ProjectResponse(project)).toList();
    }

    public ProjectResponse createProject(ProjectRequest body) {
        // Find the company using the companyId from the request
        CompanyProfile company = companyRepository.findById(body.getCompanyId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No company with this id found"));

        // Create a new Project instance with the title, description, and company
        Project newProject = new Project(body.getTitle(), body.getDescription(), company);

        // Iterate through the list of required skill IDs
        for (Integer skillId : body.getRequiredSkills()) {
            // Retrieve the Skill entity using the skillId
            Skill skill = skillRepository.findById(skillId).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No skill found with this id"));
            // Add the skill to the project's required skills list
            newProject.addRequiredSkill(skill);
        }

        List<FieldOfStudy> requiredFieldsOfStudy = body.getRequiredFieldsOfStudy().stream()
                .map(field -> {
                    try {
                        return FieldOfStudy.valueOf(field);
                    } catch (IllegalArgumentException ex) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid field of study: " + field);
                    }
                })
                .collect(Collectors.toList());

        newProject.getRequiredFieldsOfStudy().addAll(requiredFieldsOfStudy);

        // Save the project and return a ProjectResponse
        return new ProjectResponse(projectRepository.save(newProject));
    }

    public void deleteProject(int id) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No project with this id found"));

        // Remove the project from the CompanyProfile before deletion
        if (project.getCompanyProfile() != null) {
            project.getCompanyProfile().removeProject(project); // Remove the project from the company's project list
        }

        // Now, delete the project from the repository
        projectRepository.delete(project);
    }
}
