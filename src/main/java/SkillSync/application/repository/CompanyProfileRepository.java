package SkillSync.application.repository;

import SkillSync.application.entity.CompanyProfile;
import SkillSync.application.entity.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyProfileRepository extends JpaRepository<CompanyProfile, String> {
}
