package SkillSync.security.api;

import SkillSync.application.repository.CompanyProfileRepository;
import SkillSync.application.repository.StudentProfileRepository;
import SkillSync.security.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import SkillSync.security.dto.LoginRequest;
import SkillSync.security.repository.UserWithRolesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//You can enable/disable these tests in you maven builds via the maven-surefire-plugin, in your pom-file
@Tag("DisabledSecurityTest")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")  //Ensures that we use the in-memory database
@Transactional
public class AuthenticationTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  UserWithRolesRepository userWithRolesRepository;
  @Autowired
    StudentProfileRepository studentProfileRepository;
  @Autowired
  CompanyProfileRepository companyProfileRepository;


  private final ObjectMapper objectMapper = new ObjectMapper();
  public boolean isDataInitialized = false;

  @BeforeEach
  void setUp() throws Exception {
    if(!isDataInitialized) {
      isDataInitialized = true;
      TestUtils.setupTestUsers(userWithRolesRepository, studentProfileRepository, companyProfileRepository);
    }
  }

  @Test
  void login() throws Exception {
    LoginRequest loginRequest = new LoginRequest("student1", "secret");
    mockMvc.perform(post("/api/auth/login")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value("student1"))
            .andExpect(jsonPath("$.role").value("STUDENT"))
            .andExpect(result -> {
              //Not a bulletproof test, but acceptable. First part should always be the same. A token must always contain two dots.
              String token = JsonPath.read(result.getResponse().getContentAsString(), "$.token");
              assertTrue(token.startsWith("eyJhbGciOiJIUzI1NiJ9"));
              assertTrue(token.chars().filter(ch -> ch == '.').count() == 2);
            });
  }

  @Test
  void loginWithWrongPassword() throws Exception {
    LoginRequest loginRequest = new LoginRequest("student1", "wrong");
    mockMvc.perform(post("/api/auth/login")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized());

  }
  @Test
  void loginWithWrongUsername() throws Exception {
    LoginRequest loginRequest = new LoginRequest("u111111", "secret");
    mockMvc.perform(post("/api/auth/login")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized());
  }
}