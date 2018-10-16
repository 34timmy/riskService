package ru.mifi.constructor.treecontroller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mifi.constructor.model.DTO.ModelDTO;
import ru.mifi.constructor.model.Model;
import ru.mifi.constructor.service.ConstructorService;
import ru.mifi.service.risk.domain.Formula;

import javax.validation.Valid;
import java.sql.SQLException;
import java.util.List;


@RestController
@RequestMapping("/constructor")
@Api(value = "Контроллер для конструктора моделей", description = "CRUD операции для модели,правила,формулы")
public class ConstructorController {

    @Autowired
    ConstructorService constructorService;

    @ApiOperation(value = "Запустить основной расчет")
    @ResponseBody
    @GetMapping(value = "/modelDTO", produces = "application/json")
    public List<ModelDTO> getAllModelsDTO() throws SQLException {
        return constructorService.getAllModelsDTO();
    }

    @ResponseBody
    @GetMapping(value = "/model", produces = "application/json")
    public List<Model> getAllModels() throws SQLException {
        return constructorService.getAllModels();
    }


}
