package SkillSync.security.dto;


import lombok.Getter;
import lombok.Setter;
import SkillSync.security.entity.UserWithRoles;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class UserWithRolesResponse {
    String userName;
    String roleNames;
    String email;

    public UserWithRolesResponse(UserWithRoles userWithRoles){
        this.userName = userWithRoles.getUsername();
        this.roleNames = userWithRoles.getRole().toString();
        this.email = userWithRoles.getEmail();
    }

}
