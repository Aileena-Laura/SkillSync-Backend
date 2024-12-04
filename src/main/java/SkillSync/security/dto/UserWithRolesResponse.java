package SkillSync.security.dto;


import lombok.Getter;
import lombok.Setter;
import SkillSync.security.entity.UserWithRoles;

@Getter
@Setter
public class UserWithRolesResponse {
    String userName;
    String role;
    String email;

    public UserWithRolesResponse(UserWithRoles userWithRoles){
        this.userName = userWithRoles.getUsername();
        this.role = userWithRoles.getRole().toString();
        this.email = userWithRoles.getEmail();
    }

}
