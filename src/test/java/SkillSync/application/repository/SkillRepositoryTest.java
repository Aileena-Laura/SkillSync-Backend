package SkillSync.application.repository;

import SkillSync.application.entity.StudentProfile;
import SkillSync.security.TestUtils;
import SkillSync.security.repository.UserWithRolesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@DataJpaTest
public class SkillRepositoryTest {
    @Autowired
    StudentProfileRepository studentRepository;
    @Autowired
    SkillRepository skillRepository;
    @Autowired
    UserWithRolesRepository userWithRolesRepository;

    StudentProfile student;

    @BeforeEach
    void setup(){
        userWithRolesRepository.deleteAll();
        studentRepository.deleteAll();
        TestUtils.setupTestUsers(userWithRolesRepository, studentRepository);
    }


}
