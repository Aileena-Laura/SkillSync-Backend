package SkillSync.application.service;

import SkillSync.application.dto.SkillAPIResponse;
import SkillSync.application.entity.SkillAPI;
import SkillSync.application.repository.SkillAPIRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SkillAPIService {

    private final SkillAPIRepository repository;

    public SkillAPIService(SkillAPIRepository repository) {
        this.repository = repository;
    }

    public List<SkillAPIResponse> getAllSkills(){
        List<SkillAPI> skills = repository.findAll();
        return skills.stream().map(skill -> new SkillAPIResponse(skill)).toList();
    }
}
