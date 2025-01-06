package SkillSync.security.service;

import SkillSync.application.entity.CompanyProfile;
import SkillSync.application.entity.Education;
import SkillSync.application.entity.FieldOfStudy;
import SkillSync.application.entity.StudentProfile;
import SkillSync.application.repository.CompanyProfileRepository;
import SkillSync.application.repository.StudentProfileRepository;
import SkillSync.security.dto.CompanySecurityRequest;
import SkillSync.security.dto.StudentSecurityRequest;
import SkillSync.security.dto.UserWithRolesRequest;
import SkillSync.security.dto.UserWithRolesResponse;
import SkillSync.security.entity.Role;
import SkillSync.security.entity.UserWithRoles;
import SkillSync.security.repository.UserWithRolesRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserWithRolesService {

  private final UserWithRolesRepository userWithRolesRepository;
  private final StudentProfileRepository studentProfileRepository;
  private final CompanyProfileRepository companyProfileRepository;

  public UserWithRolesService(UserWithRolesRepository userWithRolesRepository, StudentProfileRepository studentProfileRepository, CompanyProfileRepository companyProfileRepository) {
    this.userWithRolesRepository = userWithRolesRepository;
    this.studentProfileRepository = studentProfileRepository;
    this.companyProfileRepository = companyProfileRepository;
  }

  public UserWithRolesResponse getUserWithRoles(String id) {
    UserWithRoles user = userWithRolesRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    return new UserWithRolesResponse(user);
  }

  public UserWithRolesResponse addRole(String username, Role role) {
    UserWithRoles user = userWithRolesRepository.findById(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    user.addRole(role);
    return new UserWithRolesResponse(userWithRolesRepository.save(user));
  }

  public UserWithRolesResponse removeRole(String username, Role role) {
    UserWithRoles user = userWithRolesRepository.findById(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    user.clearRole();
    return new UserWithRolesResponse(userWithRolesRepository.save(user));
  }

  public UserWithRolesResponse editUserWithRoles(String username, UserWithRolesRequest body) {
    UserWithRoles user = userWithRolesRepository.findById(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    user.setEmail(body.getEmail());
    validatePassword(body.getPassword()); // Validate password here
    user.setPassword(body.getPassword());
    return new UserWithRolesResponse(userWithRolesRepository.save(user));
  }

  public UserWithRolesResponse addUserWithRoles(UserWithRolesRequest body, Role role) {
    validationCheck(role, body.getUsername(), body.getEmail());
    validatePassword(body.getPassword()); // Validate password here
    String pw = body.getPassword();
    UserWithRoles userWithRoles = new UserWithRoles(body.getUsername(), pw, body.getEmail());
    userWithRoles.addRole(role);

    return new UserWithRolesResponse(userWithRolesRepository.save(userWithRoles));
  }

  public UserWithRolesResponse addStudentUser(StudentSecurityRequest body, Role role) {
    validationCheck(role, body.getUsername(), body.getEmail());
    validatePassword(body.getPassword()); // Validate password here
    String pw = body.getPassword();
    UserWithRoles userWithRoles = new UserWithRoles(body.getUsername(), pw, body.getEmail());
    userWithRoles.addRole(role);

    if (role == Role.STUDENT) {
      FieldOfStudy fieldOfStudy;
      try {
        fieldOfStudy = FieldOfStudy.valueOf(body.getFieldOfStudy().toUpperCase());
      } catch (IllegalArgumentException e) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid field of study provided");
      }

      StudentProfile studentProfile = new StudentProfile(userWithRoles, body.getFirstName(), body.getLastName());
      Education initialEducation = new Education(fieldOfStudy);
      studentProfile.setCurrentEducation(initialEducation);

      studentProfileRepository.save(studentProfile);
    }
    return new UserWithRolesResponse(userWithRolesRepository.save(userWithRoles));
  }

  public UserWithRolesResponse addCompanyUser(CompanySecurityRequest body, Role role) {
    validationCheck(role, body.getUsername(), body.getEmail());
    validatePassword(body.getPassword()); // Validate password here
    String pw = body.getPassword();
    UserWithRoles userWithRoles = new UserWithRoles(body.getUsername(), pw, body.getEmail());
    userWithRoles.addRole(role);

    if (role == Role.COMPANY) {
      companyProfileRepository.save(new CompanyProfile(userWithRoles, body.getCompanyName(), body.getWebsite(), body.getLocation()));
    }
    return new UserWithRolesResponse(userWithRolesRepository.save(userWithRoles));
  }

  private void validationCheck(Role role, String username, String email) {
    if (userWithRolesRepository.existsById(username)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user name is taken");
    }
    if (userWithRolesRepository.existsByEmail(email)) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This email is used by another user");
    }
    if (role == null) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Please specify whether you are a student or a company");
    }
  }

  private void validatePassword(String password) {
    if (password == null || password.length() < 8 || !password.matches(".*\\d.*")) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters long and contain at least one number.");
    }
  }
}
