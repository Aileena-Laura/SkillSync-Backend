package SkillSync.security;

import SkillSync.security.entity.Role;
import SkillSync.security.entity.UserWithRoles;
import SkillSync.security.repository.UserWithRolesRepository;

public class TestUtils {

  public static void setupTestUsers( UserWithRolesRepository userWithRolesRepository){
    userWithRolesRepository.deleteAll();
    String passwordUsedByAll = "secret";
    UserWithRoles user1 = new UserWithRoles("u1", passwordUsedByAll, "u1@a.dk");
    UserWithRoles user2 = new UserWithRoles("u2", passwordUsedByAll, "u2@a.dk");
    UserWithRoles user3 = new UserWithRoles("u3", passwordUsedByAll, "u3@a.dk");
    user1.addRole(Role.STUDENT);
    user2.addRole(Role.COMPANY);
    userWithRolesRepository.save(user1);
    userWithRolesRepository.save(user2);
    userWithRolesRepository.save(user3);
  }
}
