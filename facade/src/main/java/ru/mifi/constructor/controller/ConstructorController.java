package ru.mifi.constructor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mifi.constructor.model.Company;
import ru.mifi.constructor.model.DTO.ModelDTO;
import ru.mifi.constructor.model.Model;
import ru.mifi.constructor.repository.CompanyMapper;
import ru.mifi.constructor.service.ConstructorService;
import ru.mifi.service.risk.domain.Formula;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;


@RestController
@RequestMapping("/constructor")
public class ConstructorController {

    @Autowired
    CompanyMapper companyMapper;

    @Autowired
    ConstructorService constructorService;

    @GetMapping(value = "/company", produces = "application/json")
    public List<Company> getAll() throws SQLException {
        return companyMapper.getAll();
    }

    @GetMapping(value = "/modelDTO", produces = "application/json")
    public List<ModelDTO> getAllModelsDTO() throws SQLException {
        return constructorService.getAllModelsDTO();
    }

    @GetMapping(value = "/model", produces = "application/json")
    public List<Model> getAllModels() throws SQLException {
        return constructorService.getAllModels();
    }

    @PutMapping(value = "/formula", consumes = "application/json")
    public void createFormula(@Valid @RequestBody Formula formula) {
//        TODO responseEntity
        constructorService.createFormula(formula);
    }

    @PostMapping(value = "/formula", consumes = "application/json")
    public void updateFormula(@Valid @RequestBody Formula formula) {
//        TODO responseEntity
        constructorService.updateFormula(formula);
    }
}
