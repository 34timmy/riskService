package ru.mifi.constructor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mifi.constructor.model.DTO.ModelDTO;
import ru.mifi.constructor.model.Model;
import ru.mifi.constructor.repository.CompanyMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ConstructorService {

    @Autowired
    CompanyMapper companyMapper;

    public List<ModelDTO> getAllModels() throws SQLException {
        List<Model> allModels = companyMapper.getAllModels();
        List<ModelDTO> allModelsDTO = new ArrayList<>();
        for (Model model : allModels) {
            allModelsDTO.add(new ModelDTO(model));
        }
        return allModelsDTO;
    }
}
