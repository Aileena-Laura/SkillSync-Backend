package SkillSync.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {

  private String username;
  private String token;
  private String role;

  public LoginResponse(String userName, String token, String role) {
    this.username = userName;
    this.token = token;
    this.role = role;
  }
}