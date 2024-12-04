package SkillSync.security.service;

import SkillSync.application.repository.CompanyProfileRepository;
import SkillSync.application.repository.StudentProfileRepository;
import SkillSync.security.TestUtils;
import SkillSync.security.dto.UserWithRolesRequest;
import SkillSync.security.dto.UserWithRolesResponse;
import SkillSync.security.entity.Role;
import SkillSync.security.entity.UserWithRoles;
import SkillSync.security.repository.UserWithRolesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

//You can enable/disable these tests in you maven builds via the maven-surefire-plugin, in your pom-file
@Tag("DisabledSecurityTest")
@DataJpaTest
class UserWithRolesServiceTest {

  UserWithRolesService userWithRolesService;

  @Autowired
  UserWithRolesRepository userWithRolesRepository;
  @Autowired
  StudentProfileRepository studentProfileRepository;
  @Autowired
  CompanyProfileRepository companyProfileRepository;

  private boolean dataInitialized = false;

  @BeforeEach
  void setUp() {
    userWithRolesService = new UserWithRolesService(userWithRolesRepository, studentProfileRepository, companyProfileRepository);
    if(!dataInitialized) {
      userWithRolesRepository.deleteAll();
      studentProfileRepository.deleteAll();
      companyProfileRepository.deleteAll();
      TestUtils.setupTestUsers(userWithRolesRepository);
      dataInitialized = true;
    }
  }


  @Test
  void getUserWithRole() {
    UserWithRolesResponse user = userWithRolesService.getUserWithRoles("u2");
    assertTrue(user.getRole().contains("COMPANY"));
  }

  @Test
  void addRole() {
    UserWithRolesResponse user = userWithRolesService.addRole("u3", Role.STUDENT);
    assertTrue(user.getRole().contains("STUDENT"));
  }

  @Test
  void editUserWithRoles() {
    String originalPassword = userWithRolesRepository.findById("u1").get().getPassword();
    UserWithRolesRequest user1 = new UserWithRolesRequest("u1New", "new_Password", "newMail@a.dk", "COMPANY");
    UserWithRolesResponse user = userWithRolesService.editUserWithRoles("u1",user1);
    assertEquals("u1", user.getUserName());  //IMPORTANT: The username should not be changed
    assertEquals("newMail@a.dk", user.getEmail());
    UserWithRoles editedUser = userWithRolesRepository.findById("u1").get();
    assertNotEquals(originalPassword, editedUser.getPassword());
  }

  @Test
  void addUserWithRolesWithRole() {
    UserWithRolesRequest user = new UserWithRolesRequest("u5", "new_Password", "xx@x.dk", "STUDENT");
    UserWithRolesResponse newUser = userWithRolesService.addUserWithRoles(user, Role.STUDENT);
    assertTrue(newUser.getRole().contains("STUDENT"));
    assertEquals("u5", newUser.getUserName());
    assertEquals("xx@x.dk", newUser.getEmail());
  }

}