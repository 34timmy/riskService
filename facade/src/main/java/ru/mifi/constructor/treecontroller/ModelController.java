package ru.mifi.constructor.treecontroller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mifi.constructor.model.DTO.ModelDTO;
import ru.mifi.constructor.model.DTO.TreeNodeDTO;
import ru.mifi.constructor.model.Model;
import ru.mifi.constructor.service.ModelService;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/constructor/models")
@Api(value = "Контроллер для конструктора моделей", description = "CRUD операции для модели")
public class ModelController {

    @Autowired
    ModelService modelService;


    @ApiOperation(value = "Запустить основной расчет")
    @ResponseBody
    @GetMapping(value = "/modelDTO", produces = "application/json")
    public List<ModelDTO> getAllModelsDTO() throws SQLException {
        return modelService.getAllModelsDTO();
    }

    @ResponseBody
    @GetMapping(produces = "application/json")
    public List<Model> getAllModels() throws SQLException {
        return modelService.getAllModels();
    }

    @ResponseBody
    @GetMapping(value = "/toNodes",produces = "application/json")
    public List<TreeNodeDTO> getAllTreeNodeDTOs() throws SQLException {
        return modelService.getAllTreeNodeDTOs();
    }

    @PutMapping(consumes = "application/json")
    public void createModel(@Valid @RequestBody Model model) {
//        TODO responseEntity
        modelService.createModel(model);
    }

    @PostMapping(consumes = "application/json")
    public void updateModel(@Valid @RequestBody Model model) {
//        TODO responseEntity
        modelService.updateModel(model);
    }

    @DeleteMapping("/{id}")
    public void deleteModel(@PathVariable("id") String id) {
//        TODO responseEntity
        modelService.deleteModel(id);
    }
}
