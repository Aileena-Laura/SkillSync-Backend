package SkillSync.application.service;

import SkillSync.application.dto.SkillRequest;
import SkillSync.application.dto.SkillResponse;
import SkillSync.application.entity.Skill;
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
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
public class SkillServiceTest {
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
    SkillService service;
    private boolean dataInitialized = false;

    @BeforeEach
    void setup(){
        service = new SkillService(studentRepository, skillRepository, projectRepository);
        if(!dataInitialized) {
            userWithRolesRepository.deleteAll();
            studentRepository.deleteAll();
            skillRepository.deleteAll();
            TestUtils.setupTestUsers(userWithRolesRepository, studentRepository, companyProfileRepository);
            dataInitialized = true;
        }
    }

    @Test
    void addSkillSuccess(){
        SkillRequest body = SkillRequest.builder()
                .skillName("Java")
                .studentId(studentRepository.findAll().get(0).getStudentId())
                .build();
        SkillResponse response = service.addSkillToStudentProfile(body);
        assertEquals("Java", response.getSkillName());
    }

    @Test
    void testAddSkillStudentNotFountExeption(){
        SkillRequest body = SkillRequest.builder()
                .skillName("Java")
                .studentId("notValidId")
                .build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.addSkillToStudentProfile(body));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void testDeleteSkillSuccess() {
        // Step 1: Create a Skill and add it to the student's profile
        SkillRequest addSkillRequest = SkillRequest.builder()
                .skillName("Java")
                .studentId(studentRepository.findAll().get(0).getStudentId()) // Use an existing student's ID
                .build();
        // Add skill to student profile
        SkillResponse addedSkillResponse = service.addSkillToStudentProfile(addSkillRequest);
        //Check skill exists
        Skill addedSkill = skillRepository.findById(addedSkillResponse.getId()).orElseThrow();
        assertEquals("Java", addedSkill.getSkillName());

        // Step 2: Delete the Skill by its ID (from the response)
        int skillIdToDelete = addedSkillResponse.getId(); // Assuming the response contains the skillId

        // Step 3: Call the service method to delete the skill
        service.deleteSkillById(skillIdToDelete);

        // Step 4: Verify the skill is deleted by checking the repository
        Optional<Skill> deletedSkill = skillRepository.findById(skillIdToDelete);
        assertTrue(deletedSkill.isEmpty(), "Skill should be deleted and not found in the repository.");
    }

    @Test
    void testDeleteSkillNotFoundExeption(){
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> service.deleteSkillById(125));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }
}
