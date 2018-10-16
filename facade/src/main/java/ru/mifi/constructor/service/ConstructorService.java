package ru.mifi.constructor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.constructor.model.DTO.ModelDTO;
import ru.mifi.constructor.model.Model;
import ru.mifi.constructor.repository.ConstructorMapper;
import ru.mifi.service.risk.domain.Formula;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ConstructorService {

    @Autowired
    ConstructorMapper constructorMapper;

    public List<ModelDTO> getAllModelsDTO() throws SQLException {
        List<Model> allModels = constructorMapper.getAllModels();
        List<ModelDTO> allModelsDTO = new ArrayList<>();
        for (Model model : allModels) {
            allModelsDTO.add(new ModelDTO(model));
        }
        return allModelsDTO;
    }

    public List<Model> getAllModels() throws SQLException {
        return constructorMapper.getAllModels();
    }


}
