package ru.mifi.service.risk.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mifi.service.risk.database.DatabaseSelectAccessor;
import ru.mifi.service.risk.dto.CalcResultDto;
import ru.mifi.service.risk.exception.RestException;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/getData")
@Api(value = "Контроллер для получения данных", description = "Включает методы для получения данных результата расчета")
public class DataController extends ExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(DataController.class);

    @ApiOperation(value = "По конкретной таблице")
    @RequestMapping(value = "/byTable", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> byTable(
            @ApiParam(value = "Имя таблицы")
            @RequestParam("tableName") String tableName
    ) throws RestException {
        try {
            Set<CalcResultDto> result = new DatabaseSelectAccessor().getDataFromTable(tableName);
            return ResponseHelper.successResponse(result);
        } catch (Exception e) {
            throw new RestException(e);
        }
    }

    @ApiOperation(value = "По модели и листу компаний (получаем список таблиц)")
    @RequestMapping(value = "/byModelAndListId", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> byModelAndList(
            @ApiParam(value = "Id модели")
            @RequestParam("modelId") String modelId,
            @ApiParam(value = "Id списка компаний")
            @RequestParam("companyListId") String companyListId
    ) throws RestException {
        try {
            Set<String> result = new DatabaseSelectAccessor().getTablesForModelAndList(modelId, companyListId);
            return ResponseHelper.successResponse(result);
        } catch (Exception e) {
            throw new RestException(e);
        }
    }
}