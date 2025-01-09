package SkillSync.application.service;

import SkillSync.application.dto.ProjectResponse;
import SkillSync.application.entity.*;
import SkillSync.application.repository.CompanyProfileRepository;
import SkillSync.application.repository.ProjectRepository;
import SkillSync.application.repository.SkillRepository;
import SkillSync.application.repository.StudentProfileRepository;
import SkillSync.security.TestUtils;
import SkillSync.security.repository.UserWithRolesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class ProjectServiceTest {

    @Autowired
    StudentProfileRepository studentRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    UserWithRolesRepository userWithRolesRepository;
    @Autowired
    CompanyProfileRepository companyProfileRepository;
    @Autowired
    ProjectRepository projectRepository;
    ProjectService service;
    private boolean dataInitialized = false;

    @BeforeEach
    void setup(){
        service = new ProjectService(projectRepository, companyProfileRepository, skillRepository, studentRepository);
        if(!dataInitialized) {
            userWithRolesRepository.deleteAll();
            studentRepository.deleteAll();
            skillRepository.deleteAll();
            projectRepository.deleteAll();
            TestUtils.setupTestUsers(userWithRolesRepository, studentRepository, companyProfileRepository);
            dataInitialized = true;
        }
    }

    @Test
    void searchProjects() {
        // Arrange: Create test data
        CompanyProfile tesla = companyProfileRepository.findById("company4").orElseThrow();
        CompanyProfile google = companyProfileRepository.findById("company5").orElseThrow();
        StudentProfile student = studentRepository.findById("student1").orElseThrow();
        Project project1 = new Project("Project A", "Description A", tesla);
        Project project2 = new Project("Project B", "Description B", tesla);
        Project project3 = new Project("Other Project", "Different Company", google);

        projectRepository.saveAll(List.of(project1, project2, project3));

        // Act: Perform a search
        String searchTerm = "Tesla";
        Pageable pageable = PageRequest.of(0, 5);
        Map<String, Object> result = service.searchProjects(searchTerm, student.getStudentId(), pageable);

        // Assert: Check the results
        List<ProjectResponse> content = (List<ProjectResponse>) result.get("content");
        assertEquals(2, content.size()); // Only projects containing "Project" should be returned
        assertTrue(content.stream().anyMatch(p -> p.getTitle().equals("Project A")));
        assertTrue(content.stream().anyMatch(p -> p.getTitle().equals("Project B")));
    }

    @Test
    void getAllProjectsByMatch() {
        // Arrange: Create test data
        CompanyProfile tesla = companyProfileRepository.findById("company4").orElseThrow();
        // Student has the skill Java and field of study COMPUTER SCIENCE
        StudentProfile student = studentRepository.findById("student1").orElseThrow();
        Project project1 = new Project("Project A", "Description A", tesla);
        project1.addRequiredSkill(new Skill("Python"));
        project1.getRequiredFieldsOfStudy().add(FieldOfStudy.COMPUTER_SCIENCE);

        projectRepository.saveAll(List.of(project1));

        // Act: Perform a search
        String searchTerm = "Tesla";
        Pageable pageable = PageRequest.of(0, 5);
        Map<String, Object> result = service.searchProjects(searchTerm, student.getStudentId(), pageable);

        // Assert: Check the results
        List<ProjectResponse> content = (List<ProjectResponse>) result.get("content");
        // Total possible match points = 2. Actual match points = 1. 1/2 * 100 = 50
        assertEquals(50, content.get(0).getMatch());
    }

    @Test
    void getProjectsByCompanyId() {
        // Arrange: Create test data
        CompanyProfile tesla = companyProfileRepository.findById("company4").orElseThrow();
        CompanyProfile google = companyProfileRepository.findById("company5").orElseThrow();
        Project project1 = new Project("Tesla Project A", "Description A", tesla);
        Project project2 = new Project("Tesla Project B", "Description B", tesla);
        Project project3 = new Project("Google Project A", "Different Company", google);

        projectRepository.saveAll(List.of(project1, project2, project3));

        // Act: Retrieve projects by company ID
        List<ProjectResponse> projects = service.getProjectsByCompanyId(tesla.getAccountId());

        // Assert: Check results
        assertEquals(3, projectRepository.count());
        assertEquals(2, projects.size());
        assertTrue(projects.stream().anyMatch(p -> p.getTitle().equals("Tesla Project A")));
        assertTrue(projects.stream().anyMatch(p -> p.getTitle().equals("Tesla Project B")));
    }
}