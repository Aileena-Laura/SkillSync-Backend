package SkillSync.application.repository;


import SkillSync.application.entity.Skill;
import SkillSync.application.entity.SkillAPI;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillAPIRepository extends JpaRepository<SkillAPI, Integer> {
}
