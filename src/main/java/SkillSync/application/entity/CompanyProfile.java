package SkillSync.application.entity;

import SkillSync.security.entity.UserWithRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CompanyProfile {
    @Id
    private String accountId;

    private String companyName;
    private String website;
    private String location;

    @OneToOne
    @JoinColumn(name = "user_account_id")
    @MapsId
    private UserWithRoles userId;

    public CompanyProfile(UserWithRoles user, String companyName, String website, String location){
        this.userId = user;
        this.companyName = companyName;
        this.website = website;
        this.location = location;
    }
}
