package SkillSync.application.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
    public class Project {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int projectId;
        private String title;
        private String description;

        @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinTable(
                name = "project_skill",
                joinColumns = @JoinColumn(name = "project_id"),
                inverseJoinColumns = @JoinColumn(name = "skill_id")
        )
        private List<Skill> requiredSkills = new ArrayList<>();

        @ElementCollection(targetClass = FieldOfStudy.class)
        @Enumerated(EnumType.STRING)
        @CollectionTable(name = "project_field_of_study", joinColumns = @JoinColumn(name = "project_id"))
        @Column(name = "field_of_study")
        private List<FieldOfStudy> requiredFieldsOfStudy = new ArrayList<>();

        @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
        @JoinColumn(name = "company_id")
        private CompanyProfile companyProfile;

        @Enumerated(EnumType.STRING)
        private ProjectStatus status;

        public Project(String title, String description, CompanyProfile companyProfile) {
            this.title = title;
            this.description = description;
            this.companyProfile = companyProfile;
            companyProfile.addProject(this);
        }

        public void addRequiredSkill(Skill skill){
            this.requiredSkills.add(skill);
        }

        public void removeRequiredSkill(Skill skill){
            requiredSkills.remove(skill);
        }

    }
