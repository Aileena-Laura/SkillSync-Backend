package SkillSync.application.api;

import SkillSync.application.dto.CompanyRequest;
import SkillSync.application.dto.CompanyResponse;
import SkillSync.application.service.CompanyService;
import SkillSync.security.dto.CompanySecurityRequest;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/description/{id}")
    public CompanyResponse editCompanyDescription(@PathVariable String id, @RequestBody SkillSync.application.dto.CompanyRequest body){
        return companyService.editCompanyDescription(id, body);
    }

    @PatchMapping("/{id}")
    public CompanyResponse editBasicInfo(@PathVariable String id, @RequestBody CompanyRequest body){
        return companyService.editCompanyBasicInfo(id, body);
    }
}
