package ru.mifi.constructor.treecontroller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mifi.constructor.model.CompanyList;
import ru.mifi.constructor.model.DTO.TreeNodeDTO;
import ru.mifi.constructor.model.Model;
import ru.mifi.constructor.service.MainModelService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/constructor/models")
@Api(value = "Контроллер для конструктора моделей", description = "CRUD операции для модели")
public class MainModelController {

    @Autowired
    MainModelService mainModelService;


    @ResponseBody
    @GetMapping(produces = "application/json")
    public List<Model> getAllModelsOnly() throws SQLException {
        return mainModelService.getAllModelsOnly();
    }

    @ResponseBody
    @GetMapping(value = "/treeNodes", produces = "application/json")
    public List<TreeNodeDTO> getAllTreeNodeDTOs() throws SQLException {
        return mainModelService.getAllTreeNodeDTOs();
    }

    @ResponseBody
    @GetMapping(value = "/treeNodes/{modelId}", produces = "application/json")
    public List<TreeNodeDTO> getTreeNodeDTO(@PathVariable("modelId") String modelId) throws SQLException {
        return mainModelService.getTreeNodeDTO(modelId);
    }

    @ResponseBody
    @GetMapping(value = "/companyLists",produces = "application/json")
    public List<CompanyList> getAllCompanyLists() throws SQLException {
        return mainModelService.getAllCompanyLists();
    }

    @PostMapping(value = "/companyLists",consumes = "application/json")
    public void createCompanyList(@Valid @RequestBody CompanyList companyList) {
//        TODO responseEntity
        mainModelService.createCompanyList(companyList);
    }

    @PutMapping(value = "/companyLists",produces = "application/json")
    public void updateCompanyList(@Valid @RequestBody CompanyList companyList) {
//        TODO responseEntity
        mainModelService.createCompanyList(companyList);
    }

    @DeleteMapping(value = "/companyLists")
    public void deleteCompanyList(@RequestParam("id") String id)
    {
        mainModelService.deleteCompanyList(id);
    }

    @PostMapping(consumes = "application/json")
    public void createModel(@Valid @RequestBody Model model) {
//        TODO responseEntity
        mainModelService.createModel(model);
    }

    @PostMapping(value = "/copy",consumes = "application/json")
    public void copyModel(@Valid @RequestBody Model model) {
//        TODO responseEntity
        mainModelService.copyModel(model);
    }

    @PutMapping(consumes = "application/json")
    public void updateModel(@Valid @RequestBody Model model) {
//        TODO responseEntity
        mainModelService.updateModel(model);
    }

    @DeleteMapping("/{id}")
    public void deleteModel(@PathVariable("id") String id) {
//        TODO responseEntity
        mainModelService.deleteModel(id);
    }
}
