package SkillSync.application.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyRequest {
    private String companyName;
    private String description;
    private String location;
    private String email;
    private String website;
}
