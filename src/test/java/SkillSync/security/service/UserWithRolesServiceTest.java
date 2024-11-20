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
  void getUserWithRoles() {
    UserWithRolesResponse user = userWithRolesService.getUserWithRoles("u1");
    assertEquals(2, user.getRoleNames().size());
    assertTrue(user.getRoleNames().contains("STUDENT"));
    assertTrue(user.getRoleNames().contains("COMPANY"));
  }

  @Test
  void addRole() {
    UserWithRolesResponse user = userWithRolesService.addRole("u4", Role.STUDENT);
    assertEquals(1, user.getRoleNames().size());
    assertTrue(user.getRoleNames().contains("STUDENT"));
  }

  @Test
  void removeRole() {
    UserWithRolesResponse user = userWithRolesService.removeRole("u1", Role.STUDENT);
    assertEquals(1, user.getRoleNames().size());
    assertTrue(user.getRoleNames().contains("COMPANY"));
    user = userWithRolesService.removeRole("u1", Role.COMPANY);
    assertEquals(0, user.getRoleNames().size());
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
  void addUserWithRolesWithNoRole() {
    UserWithRolesRequest user = new UserWithRolesRequest("u5", "new_Password", "xx@x.dk", null);
    UserWithRolesResponse newUser = userWithRolesService.addUserWithRoles(user, null);
    assertEquals(0, newUser.getRoleNames().size());
    assertEquals("u5", newUser.getUserName());
    assertEquals("xx@x.dk", newUser.getEmail());
    //Verify that the password is hashed
    assertEquals(60,userWithRolesRepository.findById("u5").get().getPassword().length());
  }
  @Test
  void addUserWithRolesWithRole() {
    UserWithRolesRequest user = new UserWithRolesRequest("u5", "new_Password", "xx@x.dk", "STUDENT");
    UserWithRolesResponse newUser = userWithRolesService.addUserWithRoles(user, Role.STUDENT);
    assertEquals(1, newUser.getRoleNames().size());
    assertTrue(newUser.getRoleNames().contains("STUDENT"));
    assertEquals("u5", newUser.getUserName());
    assertEquals("xx@x.dk", newUser.getEmail());
  }

}