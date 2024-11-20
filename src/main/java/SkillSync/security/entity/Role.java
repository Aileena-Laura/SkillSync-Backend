package SkillSync.security.entity;

/*
Add required roles for your project to this enum
If you change anything here, you must also change this annotation, used on roles in UserWithRoleController accordingly
@Column(columnDefinition = "ENUM('USER','ADMIN')")
 */
public enum Role {
    STUDENT,
    COMPANY;

    public static Role fromString(String roleString) {
        return Role.valueOf(roleString.trim().toUpperCase());
    }
}
