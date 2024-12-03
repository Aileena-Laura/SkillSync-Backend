package SkillSync.application.repository;

import SkillSync.application.entity.Project;
import SkillSync.application.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> findByCompanyProfile_AccountId(String id);
}
