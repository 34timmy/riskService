package ru.mifi.constructor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.mifi.constructor.model.CompanyList;
import ru.mifi.constructor.model.DTO.TreeNodeDTO;
import ru.mifi.constructor.model.Model;
import ru.mifi.constructor.model.ModelCalc;
import ru.mifi.constructor.repository.ConstructorMapper;
import ru.mifi.constructor.utils.exception.NotFoundException;
import ru.mifi.service.risk.domain.Formula;

import java.lang.reflect.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class MainModelService {

    @Autowired
    ConstructorMapper constructorMapper;


    public List<Model> getAllModels() throws SQLException {
        return constructorMapper.getAllModels();
    }

    public List<Model> getAllModelsOnly() throws SQLException {
        return constructorMapper.getAllModelsOnly();
    }

    public Model getModel(String id) {
        return constructorMapper.getModel(id);
    }

    @Transactional
    public void updateModel(Model model) {
        constructorMapper.updateModel(model);
    }

    @Transactional
    public void createModel(Model model) {
        constructorMapper.createModel(model);
    }

    @Transactional
    public void copyModel(Model model) {
        Model modelFromDb = constructorMapper.getModel(model.getId());
        if (modelFromDb == null) {
            throw new NotFoundException("");
        }

        String uuid = UUID.randomUUID().toString();
        model.setId(uuid);
        constructorMapper.createModel(model);
        ArrayList<ModelCalc> modelCalcs = new ArrayList<>(modelFromDb.getModelCalcs());
        modelCalcs.forEach(modelCalc ->
        {
            modelCalc.setModel_id(uuid);
            constructorMapper.createModelCalc(modelCalc);
        });
        model.setModelCalcs(modelCalcs);


    }

    @Transactional
    public void deleteModel(String id) {
        constructorMapper.deleteModel(id);
    }

    public List<TreeNodeDTO> getAllTreeNodeDTOs() throws SQLException {
        List<Formula> allFormulas = constructorMapper.getAllFormulas();
        List<Model> allModels = getAllModels();
        TreeNodeDTO treeNodeDTO = new TreeNodeDTO(allModels, allFormulas);

        return treeNodeDTO.getResultList();
    }

    public List<CompanyList> getAllCompanyLists() throws SQLException {
        return constructorMapper.getAllCompanyLists();
    }

    public void createCompanyList(CompanyList companyList) {
        constructorMapper.createCompanyList(companyList);
    }

    public List<TreeNodeDTO> getTreeNodeDTO(String modelId) {
        List<Formula> allFormulas = constructorMapper.getAllFormulas();
        List<Model> allModels = Collections.singletonList(getModel(modelId));
        TreeNodeDTO treeNodeDTO = new TreeNodeDTO(allModels, allFormulas);

        return treeNodeDTO.getResultList();
    }

    public void deleteCompanyList(String id) {
        constructorMapper.deleteCompanyList(id);
    }


}
