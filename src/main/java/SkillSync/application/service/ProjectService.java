package SkillSync.application.service;

import SkillSync.application.dto.ProjectRequest;
import SkillSync.application.dto.ProjectResponse;
import SkillSync.application.dto.SkillRequest;
import SkillSync.application.entity.*;
import SkillSync.application.repository.CompanyProfileRepository;
import SkillSync.application.repository.ProjectRepository;
import SkillSync.application.repository.SkillRepository;
import SkillSync.application.repository.StudentProfileRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final CompanyProfileRepository companyRepository;
    private final SkillRepository skillRepository;
    private final StudentProfileRepository studentRepository;

    public ProjectService(ProjectRepository projectRepository, CompanyProfileRepository companyRepository, SkillRepository skillRepository, StudentProfileRepository studentRepository) {
        this.projectRepository = projectRepository;
        this.companyRepository = companyRepository;
        this.skillRepository = skillRepository;
        this.studentRepository = studentRepository;
    }

    public Map<String, Object> searchProjects(String term, String studentId, Pageable pageable) {
        // Fetch the student profile
        StudentProfile student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No student with this ID found"));

        // Fetch projects based on the search term
        List<Project> projects = projectRepository.findProjectsBySearchTerm(term);

        // Calculate match percentage for each project and map to ProjectResponse
        List<ProjectResponse> projectResponses = projects.stream()
                .map(project -> {
                    double matchPercentage = calculateMatchPercentage(student, project);
                    String companyName = getCompanyNameById(project.getCompanyProfile().getAccountId());
                    return new ProjectResponse(project, matchPercentage, companyName);
                })
                .sorted((response1, response2) -> Double.compare(response2.getMatch(), response1.getMatch())) // Sort by match percentage
                .toList();

        // Apply pagination manually
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), projectResponses.size());
        List<ProjectResponse> paginatedProjects = projectResponses.subList(start, end);

        // Prepare response with pagination data
        Map<String, Object> response = new HashMap<>();
        response.put("content", paginatedProjects);
        response.put("totalPages", (int) Math.ceil((double) projectResponses.size() / pageable.getPageSize()));
        response.put("currentPage", pageable.getPageNumber());
        response.put("pageSize", pageable.getPageSize());
        response.put("totalElements", projectResponses.size());

        return response;
    }

    public Map<String, Object> getAllProjectsByMatch(Pageable pageable, String studentId) {
        // Fetch the student profile
        StudentProfile student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No student with this ID found"));

        // Filter projects by field of study first
        List<Project> filteredProjects = projectRepository.findProjectsByFieldOfStudySorted(student.getCurrentEducation().getFieldOfStudy(), pageable);

        // Calculate match percentages for the projects
        List<ProjectResponse> projectResponses = filteredProjects.stream()
                .map(project -> {
                    double matchPercentage = calculateMatchPercentage(student, project);
                    String companyName = getCompanyNameById(project.getCompanyProfile().getAccountId());
                    return new ProjectResponse(project, matchPercentage, companyName);
                })
                .collect(Collectors.toList());  // Collect to list first (no sorting yet)

        //Apply pagination manually
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), projectResponses.size());
        List<ProjectResponse> paginatedProjects = projectResponses.subList(start, end);

        //Sort the paginated projects by match percentage
        paginatedProjects.sort((response1, response2) -> Double.compare(response2.getMatch(), response1.getMatch()));

        // Pagination data
        int totalProjects = projectResponses.size();
        int totalPages = (int) Math.ceil((double) totalProjects / pageable.getPageSize());

        //Prepare the response
        Map<String, Object> response = new HashMap<>();
        response.put("content", paginatedProjects);
        response.put("totalPages", totalPages);
        response.put("currentPage", pageable.getPageNumber());
        response.put("pageSize", pageable.getPageSize());
        response.put("totalElements", totalProjects);

        return response;
    }

    private String getCompanyNameById(String companyId) {
        CompanyProfile company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found"));
        return company.getCompanyName();
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

        // delete the project from the repository
        projectRepository.delete(project);
    }

    public double calculateMatchPercentage(StudentProfile student, Project project) {
        // Extract student skills and field of study
        List<Skill> studentSkills = student.getSkills();
        FieldOfStudy studentFieldOfStudy = student.getCurrentEducation().getFieldOfStudy(); // Single field of study

        // Extract project requirements
        List<Skill> projectRequiredSkills = project.getRequiredSkills();
        List<FieldOfStudy> projectRequiredFields = project.getRequiredFieldsOfStudy();

        // Calculate the match score
        int matchScore = 0;

        // Skill match: Count how many required skills the student has
        if (!projectRequiredSkills.isEmpty()) {
            for (Skill requiredSkill : projectRequiredSkills) {
                if (studentSkills.contains(requiredSkill)) {
                    matchScore++;
                }
            }
        }

        // Field of study match: Check if the student's field of study matches any required field
        if (projectRequiredFields.contains(studentFieldOfStudy)) {
            matchScore++;
        }

        // Total possible match points (+ 1 for field of study)
        int totalPossiblePoints = projectRequiredSkills.size() + 1;

        // Calculate match percentage
        double matchPercentage = 0;
        if (totalPossiblePoints > 0) {
            matchPercentage = ((double) matchScore / totalPossiblePoints) * 100;
        }

        return matchPercentage;
    }
}
