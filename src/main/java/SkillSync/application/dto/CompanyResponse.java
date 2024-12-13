package SkillSync.application.dto;

import SkillSync.application.entity.CompanyProfile;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CompanyResponse {
    String username;
    String email;
    String companyName;
    String role;
    String location;
    String description;
    String website;
    List<ProjectResponse> projects;

    public CompanyResponse(CompanyProfile companyProfile){
        this.username = companyProfile.getAccountId();
        this.email = companyProfile.getUserId().getEmail();
        this.companyName = companyProfile.getCompanyName();
        this.role = companyProfile.getUserId().getRole().toString();
        this.location = companyProfile.getLocation();
        this.website = companyProfile.getWebsite();
        this.description = companyProfile.getDescription();
        this.projects = companyProfile.getProjects().stream().map(ProjectResponse::new).toList();
    }
}
