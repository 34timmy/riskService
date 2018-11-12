package ru.mifi.constructor.treecontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.mifi.constructor.model.Company;
import ru.mifi.constructor.service.CompanyService;

import java.util.List;

@RestController
@RequestMapping("/companies")
public class CompanyController {


    @Autowired
    private CompanyService companyService;

    @GetMapping(produces = "application/json")
    @ResponseBody
    public List<Company> getAllCompanies()
    {
        return companyService.getAllCompanies();
    }


}
