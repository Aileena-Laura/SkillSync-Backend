package SkillSync.security.api;

import SkillSync.security.dto.CompanySecurityRequest;
import SkillSync.security.dto.StudentSecurityRequest;
import SkillSync.security.dto.UserWithRolesResponse;
import SkillSync.security.entity.Role;
import SkillSync.security.service.UserWithRolesService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-with-role")
public class UserWithRoleController {

  UserWithRolesService userWithRolesService;

  public UserWithRoleController(UserWithRolesService userWithRolesService) {
    this.userWithRolesService = userWithRolesService;
  }

  @PostMapping("/student")
  public UserWithRolesResponse addStudentProfile(@Valid @RequestBody StudentSecurityRequest request) {
    return userWithRolesService.addStudentUser(request, Role.fromString(request.getRole()));
  }

  @PostMapping("/company")
  public UserWithRolesResponse addCompanyProfile(@Valid @RequestBody CompanySecurityRequest request) {
    return userWithRolesService.addCompanyUser(request, Role.fromString(request.getRole()));
  }
}
