package SkillSync.application.entity;

import SkillSync.application.dto.StudentResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int educationId;

    private String degree;  // Degree type (e.g., Bachelor's, Master's, PhD)
    private String fieldOfStudy;  // Field of study (e.g., Computer Science, Marketing)


    public Education(String degree, String fieldOfStudy) {
        this.degree = degree;
        this.fieldOfStudy = fieldOfStudy;
    }
}
