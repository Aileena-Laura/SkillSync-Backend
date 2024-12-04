package SkillSync.security.api;

import SkillSync.security.service.UserDetailsServiceImp;
import SkillSync.security.dto.LoginRequest;
import SkillSync.security.dto.LoginResponse;
import SkillSync.security.entity.UserWithRoles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.joining;

@RestController
@RequestMapping("/api/auth/")
@CrossOrigin(origins = "https://skillsync.lauraramgil.dk")
public class AuthenticationController {

  @Value("${app.token-issuer}")
  private String tokenIssuer;

  @Value("${app.token-expiration}")
  private long tokenExpiration;

  private AuthenticationManager authenticationManager;

  JwtEncoder encoder;
  public AuthenticationController(AuthenticationManager authenticationManager, JwtEncoder encoder) {
    this.authenticationManager = authenticationManager;
    this.encoder = encoder;
  }

  @PostMapping("login")
  public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
    try {
      // Create authentication token with username and password
      UsernamePasswordAuthenticationToken uat = new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword());

      // Authenticate user
      Authentication authentication = authenticationManager.authenticate(uat);

      // Get authenticated user
      UserWithRoles user = (UserWithRoles) authentication.getPrincipal();
      Instant now = Instant.now();

      // Ensure the user has exactly one role
      if (authentication.getAuthorities().size() != 1) {
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User must have exactly one role to log in.");
      }

      // Extract the single role from the authorities
      String role = authentication.getAuthorities().iterator().next().getAuthority();

      // Generate JWT claims
      JwtClaimsSet claims = JwtClaimsSet.builder()
              .issuer(tokenIssuer)
              .issuedAt(now)
              .expiresAt(now.plusSeconds(tokenExpiration))
              .subject(user.getUsername())
              .claim("role", role)  // Store the single role
              .build();

      // Generate JWT token
      JwsHeader jwsHeader = JwsHeader.with(() -> "HS256").build();
      String token = encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();

      // Return the response with username, token, and role
      return ResponseEntity.ok()
              .body(new LoginResponse(user.getUsername(), token, role));

    } catch (BadCredentialsException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UserDetailsServiceImp.WRONG_USERNAME_OR_PASSWORD);
    } catch (ResponseStatusException e) {
      throw e;  // Rethrow the unauthorized exception for invalid role count
    }
  }
}
