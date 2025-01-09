package SkillSync.security.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Configurable
@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DISCRIMINATOR_TYPE")
public class UserWithRoles implements UserDetails {

  /*
  This is not very elegant since the password encoder is hardcoded, and eventually could end as being different from the one used in the project
  It's done this way, to make it easier to use this semester, since this class hashes and salts passwords automatically
  Also it's done like this since YOU CANNOT inject beans into entities
   */
  @Transient
  private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Id
  @Column(nullable = false,length = 50,unique = true)
  String username;

  @Column(nullable = false,length = 50,unique = true)
  String email;

  //60 = length of a bcrypt encoded password
  @Column(nullable = false, length = 60)
  String password;

  private boolean enabled= true;

  @CreationTimestamp
  private LocalDateTime created;

  @UpdateTimestamp
  private LocalDateTime edited;

  @Enumerated(EnumType.STRING)
  @Column(columnDefinition = "ENUM('STUDENT','COMPANY')")
  @CollectionTable(name = "security_role")
  private Role role;

  public UserWithRoles() {}


  public UserWithRoles(String user, String password, String email){
    this.username = user;
    setPassword(password);
    this.email = email;
  }

  public void setPassword(String pw){
    this.password = passwordEncoder.encode(pw);
  }


  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    if (role == null) {
      return Collections.emptyList(); // Return an empty list if no role is assigned
    }
    return List.of(new SimpleGrantedAuthority(role.toString())); // Return the single role as a GrantedAuthority
  }

  public void addRole(Role roleToAdd) {
    if (role == null || !role.equals(roleToAdd)) {
      role = roleToAdd;
    }
  }

  public void clearRole() {
    this.role = null; // Reset the role to no value (null)
  }

  //You can, but are NOT expected to use the fields below
  @Override
  public boolean isAccountNonExpired() {return enabled;}

  @Override
  public boolean isAccountNonLocked() { return enabled;}

  @Override
  public boolean isCredentialsNonExpired() { return enabled; }
}