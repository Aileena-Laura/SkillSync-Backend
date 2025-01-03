package SkillSync.application.service;

import SkillSync.application.dto.CompanyRequest;
import SkillSync.application.dto.CompanyResponse;
import SkillSync.application.dto.StudentRequest;
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

    public CompanyResponse getCompanyProfile(String id, boolean includeAll){
        CompanyProfile company = companyProfileRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Company not found"));
        return new CompanyResponse(company, includeAll);
    }

    public CompanyResponse editCompanyDescription(String id, CompanyRequest body){
        CompanyProfile company = companyProfileRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No company with this id found"));

        company.setDescription(body.getDescription());
        return new CompanyResponse(companyProfileRepository.save(company), true);
    }

    public CompanyResponse editCompanyBasicInfo(String id, CompanyRequest body){
        CompanyProfile company = companyProfileRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No company with this id found"));
        company.setCompanyName(body.getCompanyName());
        company.setLocation(body.getLocation());
        company.setWebsite(body.getWebsite());
        company.getUserId().setEmail(body.getEmail());

        return new CompanyResponse(companyProfileRepository.save(company), true);
    }

    public void deleteCompanyAccount(String id){
        CompanyProfile company = companyProfileRepository.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
        companyProfileRepository.delete(company);
    }
}
