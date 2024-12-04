package SkillSync.security;

import SkillSync.application.entity.StudentProfile;
import SkillSync.application.repository.StudentProfileRepository;
import SkillSync.security.entity.Role;
import SkillSync.security.entity.UserWithRoles;
import SkillSync.security.repository.UserWithRolesRepository;

public class TestUtils {

  public static void setupTestUsers(UserWithRolesRepository userWithRolesRepository, StudentProfileRepository studentProfileRepository){
    studentProfileRepository.deleteAll();
    userWithRolesRepository.deleteAll();
    String passwordUsedByAll = "secret";
    UserWithRoles user1 = new UserWithRoles("student1", passwordUsedByAll, "s1@a.dk");
    UserWithRoles user2 = new UserWithRoles("student2", passwordUsedByAll, "s2@a.dk");
    UserWithRoles user3 = new UserWithRoles("student3", passwordUsedByAll, "s3@a.dk");
    user1.addRole(Role.STUDENT);
    user2.addRole(Role.COMPANY);
    StudentProfile student1 = new StudentProfile(user1, "Laura", "Ramgil");
    StudentProfile student2 = new StudentProfile(user2, "Aileena", "Graichen");
    studentProfileRepository.save(student1);
    studentProfileRepository.save(student2);
    userWithRolesRepository.save(user3);
  }
}
