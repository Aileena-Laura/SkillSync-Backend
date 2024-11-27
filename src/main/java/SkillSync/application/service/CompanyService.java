package SkillSync.application.service;

import SkillSync.application.dto.CompanyResponse;
import SkillSync.application.dto.StudentResponse;
import SkillSync.application.entity.CompanyProfile;
import SkillSync.application.entity.StudentProfile;
import SkillSync.application.repository.CompanyProfileRepository;
import SkillSync.application.repository.StudentProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CompanyService {
    private final CompanyProfileRepository companyProfileRepository;

    public CompanyService(CompanyProfileRepository companyProfileRepository){
        this.companyProfileRepository = companyProfileRepository;
    }

    public CompanyResponse getCompanyProfile(String id){
        CompanyProfile company = companyProfileRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Company not found"));
        return new CompanyResponse(company);
    }
}
