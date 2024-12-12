package SkillSync.application.dto;

import SkillSync.application.entity.FieldOfStudy;
import SkillSync.application.entity.Project;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectResponse {

    private int id;
    private String title;
    private String description;
    private String companyId;
    private List<SkillResponse> requiredSkills;
    private List<FieldOfStudy> requiredFieldsOfStudy;


    public ProjectResponse(Project project) {
        this.id = project.getProjectId();
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.companyId = project.getCompanyProfile().getAccountId();
        this.requiredSkills = project.getRequiredSkills().stream().map(SkillResponse::new).toList();
        this.requiredFieldsOfStudy = project.getRequiredFieldsOfStudy();
    }
}
