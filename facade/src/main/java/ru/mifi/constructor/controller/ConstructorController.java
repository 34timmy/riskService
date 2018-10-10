package ru.mifi.constructor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mifi.constructor.model.Company;
import ru.mifi.constructor.model.DTO.ModelDTO;
import ru.mifi.constructor.repository.CompanyMapper;
import ru.mifi.constructor.service.ConstructorService;

import java.sql.SQLException;
import java.util.List;


@RestController("/constructor")
public class ConstructorController {

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    ConstructorService constructorService;

    @GetMapping(value = "/company", produces = "application/json")
    public List<Company> getAll() throws SQLException {
        return companyMapper.getAll();
    }

    @GetMapping(value = "/model", produces = "application/json")
    public List<ModelDTO> getAllModels() throws SQLException {
        return constructorService.getAllModels();
    }


}
