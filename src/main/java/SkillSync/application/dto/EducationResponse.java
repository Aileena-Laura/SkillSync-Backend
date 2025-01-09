package SkillSync.application.dto;

import SkillSync.application.entity.Education;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EducationResponse {
    private int id;
    private String educationLevel;
    private String fieldOfStudy;
    private String institutionName;
    private int graduationYear;

    // Constructor that takes an Education object
    public EducationResponse(Education education) {
        this.id = education.getId();
        this.educationLevel = education.getEducationLevel() != null ? education.getEducationLevel().toString() : "Unknown";
        this.fieldOfStudy = education.getFieldOfStudy() != null ? education.getFieldOfStudy().getFormattedName() : "Unknown";
        this.institutionName = education.getInstitutionName();
        this.graduationYear = education.getGraduationYear();
    }
}
