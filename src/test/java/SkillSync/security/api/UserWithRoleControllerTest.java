package SkillSync.security.api;

import SkillSync.application.entity.FieldOfStudy;
import SkillSync.application.repository.CompanyProfileRepository;
import SkillSync.application.repository.StudentProfileRepository;
import SkillSync.security.TestUtils;
import SkillSync.security.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
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
      TestUtils.setupTestUsers(userWithRolesRepository, studentProfileRepository, companyProfileRepository);
      companyToken = loginAndGetToken("student1", "secret");
      studentToken = loginAndGetToken("student2", "secret");
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
    StudentSecurityRequest studentRequest = new StudentSecurityRequest("u100", "secret", "u100@a.dk", null, "Laura", "Ramgil", FieldOfStudy.BUSINESS.toString());

    // Act & Assert: Perform the request and expect failure due to validation
    mockMvc.perform(post("/api/user-with-role/student")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(studentRequest)))
            .andExpect(status().isBadRequest()); // Expecting 400 Bad Request
  }

  @Test
  void addUserWithRolesStudent() throws Exception {
    StudentSecurityRequest newStudentReq = new StudentSecurityRequest("u100", "secret", "u100@a.dk", "STUDENT", "Laura", "Ramgil", FieldOfStudy.COMPUTER_SCIENCE.toString());
    mockMvc.perform(post("/api/user-with-role/student")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(newStudentReq)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName").value("u100"))
            .andExpect(jsonPath("$.email").value("u100@a.dk"))
            .andExpect(jsonPath("$.role").value("STUDENT"));
  }

  @Test
  void addUserWithRolesCompany() throws Exception {
    CompanySecurityRequest newCompanyReq = new CompanySecurityRequest("u100", "secret", "u100@a.dk", "COMPANY", "Tesla", "Tesla.com", "Ringsted");
    mockMvc.perform(post("/api/user-with-role/company")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(newCompanyReq)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userName").value("u100"))
            .andExpect(jsonPath("$.email").value("u100@a.dk"))
            .andExpect(jsonPath("$.role").value("COMPANY"));
  }
}