package ru.mifi.constructor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mifi.constructor.model.Company;
import ru.mifi.constructor.model.CompanyData;
import ru.mifi.constructor.model.Industry;
import ru.mifi.constructor.repository.ConstructorMapper;

import java.util.List;

@Service
public class CompanyService {

    @Autowired
    private ConstructorMapper constructorMapper;

    public List<Company> getAllCompanies() {
        return constructorMapper.getAllCompanies();
    }

    public List<CompanyData> getDataForCompany(String companyId) {
        return constructorMapper.getAllDataForCompany(companyId);
    }

    public List<Industry> getAllIndustries() {
        return constructorMapper.getAllIndustries();


    }
}
