package SkillSync.application.dto;

import SkillSync.application.entity.Project;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

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


    public ProjectResponse(Project project) {
        this.title = project.getTitle();
        this.description = project.getDescription();
        this.companyId = project.getCompanyProfile().getAccountId();
    }
}
