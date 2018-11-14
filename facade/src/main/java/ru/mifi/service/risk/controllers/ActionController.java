package ru.mifi.service.risk.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import ru.mifi.service.risk.domain.CalculationParamKey;
import ru.mifi.service.risk.exception.RestException;
import ru.mifi.service.risk.utils.DataService;

import java.util.Map;

@RestController
@RequestMapping(value = "/perform")
@Api(value = "Контроллер для осуществления основных действий", description = "Запускает расчет и т.д.")
public class ActionController extends ExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(ActionController.class);

    @Autowired
    @Setter
    private DataService dataService;

    @ApiOperation(value = "Запустить основной расчет")
    @RequestMapping(value = "/calculation", method =  RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> calculate(
            @ApiParam(value = "Id модели")
            @RequestParam("modelId") String modelId,
            @ApiParam(value = "Id списка компаний")
            @RequestParam("companyListId") String companyListId,
            @ApiParam(value = "Id списка компаний всей отрасли")
            @RequestParam("industryCompanyListId") String industryCompanyListId,
            @ApiParam(value = "Год для расчета")
            @RequestParam("year") Integer year
    ) throws RestException {
        CalculationParamKey key = new CalculationParamKey(modelId, companyListId, industryCompanyListId, year);
        LOG.info("Получен запрос на расчет по параметрам: " + key);
        Map<String, Object> result = dataService.performCalculation(key);
        return ResponseHelper.successResponse(result);

    }

}