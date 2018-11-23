package ru.mifi.service.risk.controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.mifi.service.risk.database.DatabaseForWeightsCalcAccessor;
import ru.mifi.service.risk.dto.ModelCalcDto;
import ru.mifi.service.risk.dto.ModelDto;
import ru.mifi.service.risk.exception.RestException;

import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping(value = "/utility/weight/")
@Api(
        value = "Контроллер для взаимодействия с утилитой по расчету весов нод модели",
        description = "Для взаимодействия через REST с десктопной C# утилитой"
)
public class WeightUtilityController {

    private static final Logger LOG = LoggerFactory.getLogger(ActionController.class);

    Gson gson = new Gson();

    @Autowired
    private DatabaseForWeightsCalcAccessor accessor;

    @ApiOperation(value = "Получить список моделей с их описанием")
    @RequestMapping(value = "/getModels", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<String> getModels() throws RestException, SQLException {
        Collection<ModelDto> toSend = accessor.init().getAllModels();
        String resultJson = gson.toJson(toSend);
        return new ResponseEntity<>(resultJson, HttpStatus.OK);
    }

    @ApiOperation(value = "Получить все ноды иерархии модели по идентификатору модели")
    @RequestMapping(value = "/getModelCalcByModelId", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseEntity<String> getModelCalcByModelId(
            @ApiParam(value = "Идентификатор модели")
            @RequestParam("modelId") String modelId
    ) throws RestException, SQLException {
        Collection<ModelCalcDto> toSend = accessor.init().getAllNodesByModelId(modelId);
        String resultJson = gson.toJson(toSend);
        return new ResponseEntity<>(resultJson, HttpStatus.OK);
    }

    @ApiOperation(value = "Обновить веса и экспертные оценки узлов модели")
    @RequestMapping(value = "/updateWeights", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseEntity<String> updateWeights(
            @ApiParam(value = "Json с массивом нод для обновления")
            @RequestParam("jsonToUpdate") String jsonToUpdate
    ) throws RestException, SQLException {
        Type type = new TypeToken<List<ModelCalcDto>>() {
        }.getType();
        List<ModelCalcDto> read = gson.fromJson(jsonToUpdate, type);
        accessor.init().updateWeightsOfModelCalc(read);
        return new ResponseEntity<>("{\"status\": \"success\"}", HttpStatus.OK);
    }
}
