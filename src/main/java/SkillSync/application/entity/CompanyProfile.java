package SkillSync.application.entity;

import SkillSync.security.entity.UserWithRoles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CompanyProfile {
    @Id
    @Column(name = "company_id")
    private String companyId;

    private String companyName;
    private String website;
    private String description;
    private String location;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_account_id")
    @MapsId
    private UserWithRoles userId;

    @OneToMany(mappedBy = "companyProfile", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Project> projects = new ArrayList<>();

    public CompanyProfile(UserWithRoles user, String companyName, String website, String location){
        this.userId = user;
        this.companyName = companyName;
        this.website = website;
        this.location = location;
    }

    public void addProject(Project project){
        this.projects.add(project);
    }

    public void removeProject(Project project) {
        this.projects.remove(project);
    }
}
