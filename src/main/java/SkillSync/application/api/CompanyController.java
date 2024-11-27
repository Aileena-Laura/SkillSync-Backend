package SkillSync.application.api;

import SkillSync.application.dto.CompanyResponse;
import SkillSync.application.dto.StudentResponse;
import SkillSync.application.service.CompanyService;
import SkillSync.application.service.StudentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/company")
public class CompanyController {
    CompanyService companyService;

    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @GetMapping("/{username}")
    public CompanyResponse getCompanyProfileByUsername(@PathVariable String username){
        return companyService.getCompanyProfile(username);
    }
}
