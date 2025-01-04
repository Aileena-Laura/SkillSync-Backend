package SkillSync.application.service;

import SkillSync.application.repository.CompanyProfileRepository;
import SkillSync.application.repository.SkillRepository;
import SkillSync.application.repository.StudentProfileRepository;
import SkillSync.security.TestUtils;
import SkillSync.security.repository.UserWithRolesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@DataJpaTest
public class CompanyProfileServiceTest {
    @Autowired
    StudentProfileRepository studentRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    UserWithRolesRepository userWithRolesRepository;
    @Autowired
    CompanyProfileRepository companyProfileRepository;
    CompanyService service;
    private boolean dataInitialized = false;

    @BeforeEach
    void setup(){
        service = new CompanyService(companyProfileRepository);
        if(!dataInitialized) {
            userWithRolesRepository.deleteAll();
            studentRepository.deleteAll();
            skillRepository.deleteAll();
            TestUtils.setupTestUsers(userWithRolesRepository, studentRepository, companyProfileRepository);
            dataInitialized = true;
        }
    }

    @Test
    void deleteCompanyDeletesUserWithRole(){
        assertEquals(2, studentRepository.count());
        assertEquals(2, companyProfileRepository.count());
        assertEquals(5, userWithRolesRepository.count());

        String usernameToDelete = "company4";
        service.deleteCompanyAccount(usernameToDelete);

        assertFalse(userWithRolesRepository.findById(usernameToDelete).isPresent());
        assertEquals(1, companyProfileRepository.count());
        assertEquals(4, userWithRolesRepository.count());

    }
}
