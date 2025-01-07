package SkillSync.application.repository;

import SkillSync.application.entity.FieldOfStudy;
import SkillSync.application.entity.Project;
import SkillSync.application.entity.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByCompanyProfile_AccountId(String id);

    @Query("SELECT p FROM Project p " +
            "ORDER BY CASE WHEN :studentFieldOfStudy IN (SELECT f FROM p.requiredFieldsOfStudy f) THEN 1 ELSE 0 END DESC")
    List<Project> findProjectsByFieldOfStudySorted(@Param("studentFieldOfStudy") FieldOfStudy studentFieldOfStudy);
    ;

    @Query("SELECT p FROM Project p " +
            "LEFT JOIN p.requiredSkills s " +
            "LEFT JOIN p.companyProfile c " +
            "WHERE (:term IS NULL OR " +
            "LOWER(s.skillName) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(c.companyName) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(c.location) LIKE LOWER(CONCAT('%', :term, '%')))")
    List<Project> findProjectsBySearchTerm(@Param("term") String term);


}
