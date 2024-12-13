package SkillSync.application.service;

import SkillSync.application.dto.SkillRequest;
import SkillSync.application.dto.SkillResponse;
import SkillSync.application.entity.*;
import SkillSync.application.repository.ProjectRepository;
import SkillSync.application.repository.SkillRepository;
import SkillSync.application.repository.StudentProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SkillService {
    StudentProfileRepository studentRepository;
    SkillRepository skillRepository;

    ProjectRepository projectRepository;

    public SkillService(StudentProfileRepository studentProfileRepository, SkillRepository skillRepository, ProjectRepository projectRepository) {
        this.studentRepository = studentProfileRepository;
        this.skillRepository = skillRepository;
        this.projectRepository = projectRepository;
    }

    public List<SkillResponse> getAllSkills(){
        List<Skill> skills = skillRepository.findAll();
        return skills.stream().map(skill -> new SkillResponse(skill)).toList();
    }

    public SkillResponse addSkillToStudentProfile(SkillRequest body){
        StudentProfile student = studentRepository.findById(body.getStudentId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No student with this id found"));
        Skill newSkill = new Skill(body.getSkillName());
        student.addSkill(newSkill);
        return new SkillResponse(skillRepository.save(newSkill));
    }

    public SkillResponse addSkillToProject(SkillRequest body){
        Project project = projectRepository.findById(body.getProjectId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No project with this id found"));
        Skill newSkill = new Skill(body.getSkillName());
        project.addRequiredSkill(newSkill);
        return new SkillResponse(skillRepository.save(newSkill));
    }

    public void deleteSkillById(int id){
        Skill skill = skillRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No skill with this id found"));
        skillRepository.delete(skill);
    }
}
