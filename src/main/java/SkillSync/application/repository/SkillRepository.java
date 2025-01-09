package SkillSync.application.repository;

import SkillSync.application.entity.Skill;
import SkillSync.application.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
}
