package SkillSync.security.api;

import SkillSync.application.repository.CompanyProfileRepository;
import SkillSync.application.repository.StudentProfileRepository;
import SkillSync.security.dto.StudentSecurityRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import SkillSync.security.TestUtils;
import SkillSync.security.dto.LoginRequest;
import SkillSync.security.dto.LoginResponse;
import SkillSync.security.dto.UserWithRolesRequest;
import SkillSync.security.repository.UserWithRolesRepository;
import SkillSync.security.service.UserWithRolesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//You can enable/disable these tests in you maven builds via the maven-surefire-plugin, in your pom-file
@Tag("DisabledSecurityTest")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
//@Import(PasswordEncoderConfig.class)
@Transactional
class UserWithRoleControllerTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  UserWithRolesRepository userWithRolesRepository;
  @Autowired
  UserWithRolesService userWithRolesService;
  @Autowired
  StudentProfileRepository studentProfileRepository;
  @Autowired
  CompanyProfileRepository companyProfileRepository;
  @Autowired
  PasswordEncoder passwordEncoder;

  String companyToken;
  String studentToken;
  private final ObjectMapper objectMapper = new ObjectMapper();

  private boolean dataInitialized = false;

  @BeforeEach
  void setUp() throws Exception {
    userWithRolesService = new UserWithRolesService(userWithRolesRepository, studentProfileRepository, companyProfileRepository);
    if (!dataInitialized) {
      userWithRolesRepository.deleteAll();
      studentProfileRepository.deleteAll();
      companyProfileRepository.deleteAll();
      TestUtils.setupTestUsers(userWithRolesRepository);
      companyToken = loginAndGetToken("u2", "secret");
      studentToken = loginAndGetToken("u1", "secret");
      dataInitialized = true;
    }
  }

  String loginAndGetToken(String user, String pw) throws Exception {
    LoginRequest loginRequest = new LoginRequest(user, pw);
    MvcResult response = mockMvc.perform(post("/api/auth/login")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn();
    LoginResponse loginResponse = objectMapper.readValue(response.getResponse().getContentAsString(), LoginResponse.class);
    return loginResponse.getToken();
  }

  @Test
  void addUsersWithRolesNoRolesShouldFail() throws Exception {
    // Arrange: Create a request without roles
    StudentSecurityRequest studentRequest = new StudentSecurityRequest("u100", "secret", "u100@a.dk", null, "Laura", "Ramgil");

    // Act & Assert: Perform the request and expect failure due to validation
    mockMvc.perform(post("/api/user-with-role/student")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(studentRequest)))
            .andExpect(status().isBadRequest()); // Expecting 400 Bad Request
  }

  @Test
  void addUserWithRoles() throws Exception {
    UserWithRolesRequest newUserReq = new UserWithRolesRequest("u100", "secret", "u100@a.dk", "STUDENT");
    mockMvc.perform(post("/api/user-with-role/student")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(newUserReq)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName").value("u100"))
            .andExpect(jsonPath("$.email").value("u100@a.dk"))
            .andExpect(jsonPath("$.role").value("STUDENT"));
  }

  /*@Test
  void addRoleFailsWithWrongRole() throws Exception {
    mockMvc.perform(patch("/api/user-with-role/add-role/u2/admin")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + userToken)
                    .accept("application/json"))
            .andExpect(status().isForbidden());
  }

  @Test
  void removeRoleFailsWhenNotAuthenticatedWithRole() throws Exception {
    mockMvc.perform(patch("/api/user-with-role/remove-role/u2/user")
                    .accept("application/json"))
            .andExpect(status().isUnauthorized());
  }*/

/*  @Test
  void removeRole() throws Exception {
    mockMvc.perform(patch("/api/user-with-role/remove-role/u1/user")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + adminToken)
                    .accept("application/json"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName").value("u1"))
            .andExpect(jsonPath("$.roleNames", hasSize(1)))
            .andExpect(jsonPath("$.roleNames", contains("ADMIN")));
  }*/
}