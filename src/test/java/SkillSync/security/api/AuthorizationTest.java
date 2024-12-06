package SkillSync.security.api;


import SkillSync.application.repository.CompanyProfileRepository;
import SkillSync.application.repository.StudentProfileRepository;
import SkillSync.security.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import SkillSync.security.dto.LoginRequest;
import SkillSync.security.dto.LoginResponse;
import SkillSync.security.repository.UserWithRolesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//You can enable/disable these tests in you maven builds via the maven-surefire-plugin, in your pom-file
@Tag("DisabledSecurityTest")
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class AuthorizationTest {

  @Autowired
  MockMvc mockMvc;
  @Autowired
  UserWithRolesRepository userWithRolesRepository;
  @Autowired
  StudentProfileRepository studentProfileRepository;
  @Autowired
  CompanyProfileRepository companyProfileRepository;


  private final ObjectMapper objectMapper = new ObjectMapper();

  public static String userCompanyJwtToken;
  public static String userStudentJwtToken;
  public static String userNoRolesJwtToken;

  void LoginAndGetTokens() throws Exception {
    userStudentJwtToken = loginAndGetToken("u1","secret");
    userCompanyJwtToken = loginAndGetToken("u2","secret");
    userNoRolesJwtToken = loginAndGetToken("u3","secret");
  }

  @BeforeEach
  void setUp() throws Exception {
    TestUtils.setupTestUsers(userWithRolesRepository, studentProfileRepository, companyProfileRepository);
    if(userStudentJwtToken == null) {
      LoginAndGetTokens();
    }
  }

  String loginAndGetToken(String user,String pw) throws Exception {
    LoginRequest loginRequest = new LoginRequest(user,pw);
    MvcResult response = mockMvc.perform(post("/api/auth/login")
                    .contentType("application/json")
                    .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andReturn();
    LoginResponse loginResponse = objectMapper.readValue(response.getResponse().getContentAsString(), LoginResponse.class);
    return loginResponse.getToken();
  }
}