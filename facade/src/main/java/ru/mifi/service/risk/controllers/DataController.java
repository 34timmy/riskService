package ru.mifi.service.risk.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mifi.service.risk.database.DatabaseSelectAccessor;
import ru.mifi.service.risk.domain.CalculationParamKey;
import ru.mifi.service.risk.domain.ResultDataMapper;
import ru.mifi.service.risk.dto.CalcResultDto;
import ru.mifi.service.risk.exception.RestException;
import ru.mifi.service.risk.utils.DataService;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping(value = "/getData")
@Api(value = "Контроллер для получения данных", description = "Включает методы для получения данных результата расчета")
public class DataController extends ExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(DataController.class);

    @Autowired
    private DatabaseSelectAccessor accessor;

    @Autowired
    private DataService service;

    @ApiOperation(value = "По конкретной таблице")
    @RequestMapping(value = "/byTable", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> loadResultFromTable(
            @ApiParam(value = "Имя таблицы")
            @RequestParam("tableName") String tableName
    ) throws RestException {
        Map<String, List<CalcResultDto>> result = accessor.getDataFromTableAsList(tableName);
        return ResponseHelper.successResponse(result);
    }

    @ApiOperation(value = "Получить данные последнего расчета по параметрам")
    @RequestMapping(value = "/lastCalcByParams", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getLastCalculationResult(
            @ApiParam(value = "Id модели")
            @RequestParam("modelId") String modelId,
            @ApiParam(value = "Id списка компаний")
            @RequestParam("companyListId") String companyListId,
            @ApiParam(value = "Id списка компаний всей отрасли")
            @RequestParam("industryCompanyListId") String industryCompanyListId,
            @ApiParam(value = "Год для расчета")
            @RequestParam("year") Integer year
    ) {
        CalculationParamKey curParams = new CalculationParamKey(modelId, companyListId, industryCompanyListId, year);
        LOG.info("Получен запрос на получение данных расчета: " + curParams);
        Map<String, CalcResultDto> result = service.getCalculationResultFromDb(curParams);
        return ResponseHelper.successResponse(result);

    }

    @ApiOperation(value = "По параметрам расчета получаем список таблиц с результатами по этим параметрам")
    @RequestMapping(value = "/allTablesByParams", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getTableList(
            @ApiParam(value = "Id модели")
            @RequestParam("modelId") String modelId,
            @ApiParam(value = "Id списка компаний")
            @RequestParam("companyListId") String companyListId,
            @ApiParam(value = "Id списка компаний всей отрасли")
            @RequestParam("allCompaniesListId") String industryCompanyListId,
            @ApiParam(value = "Год для расчета")
            @RequestParam("year") Integer year
    ) throws RestException {
        CalculationParamKey curParams = new CalculationParamKey(modelId, companyListId, industryCompanyListId, year);
        LOG.info("Получен запрос для получения таблиц с результатами по параметрам: " + curParams);
        Set<String> result = accessor.getTablesForModelAndList(curParams);
        return ResponseHelper.successResponse(result);

    }

    @ApiOperation(value = "Получить список всех названий таблиц с расчётами")
    @GetMapping(value = "/getListOfResults", produces = "application/json")
    @ResponseBody
    public Map<String, Object> getListOfResults() {
        Set<ResultDataMapper> result = accessor.getTableNamesForCalcResult();
        return ResponseHelper.successResponse(result);
    }
}