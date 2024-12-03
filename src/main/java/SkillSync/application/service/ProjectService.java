package SkillSync.application.service;

import SkillSync.application.dto.ProjectRequest;
import SkillSync.application.dto.ProjectResponse;
import SkillSync.application.entity.CompanyProfile;
import SkillSync.application.entity.Project;
import SkillSync.application.repository.CompanyProfileRepository;
import SkillSync.application.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CompanyProfileRepository companyRepository;

    public ProjectService(ProjectRepository projectRepository, CompanyProfileRepository companyRepository) {
        this.projectRepository = projectRepository;
        this.companyRepository = companyRepository;
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

    public ProjectResponse createProject(ProjectRequest body){
        CompanyProfile company = companyRepository.findById(body.getCompanyId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No company with this id found"));

        Project newProject = new Project(body.getTitle(), body.getDescription(), company);
        return new ProjectResponse(projectRepository.save(newProject));
    }
}
