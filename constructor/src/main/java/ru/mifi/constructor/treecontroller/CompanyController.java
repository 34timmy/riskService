package ru.mifi.constructor.treecontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mifi.constructor.model.Company;
import ru.mifi.constructor.model.CompanyData;
import ru.mifi.constructor.model.Industry;
import ru.mifi.constructor.service.CompanyService;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {


    @Autowired
    private CompanyService companyService;

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }


    @GetMapping(value = "/companyData", produces = "application/json")
    List<CompanyData> getAllDataForCompany(@RequestParam("companyId") String companyId) {
        return companyService.getDataForCompany(companyId);
    }

    @GetMapping(value = "/industries", produces = "application/json")
    List<Industry> getAllIndustries() {
        return companyService.getAllIndustries();
    }
}
