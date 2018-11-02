package ru.mifi.service.risk.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mifi.service.risk.exception.RestException;
import ru.mifi.service.risk.utils.DataService;

import javax.annotation.Resource;
import javax.sql.DataSource;
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
    @RequestMapping(value = "/calculation", method = {RequestMethod.POST, RequestMethod.GET})
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
        try {
            LOG.info(String.format(
                    "Получен запрос на расчет по параметрам: " +
                            "\n\tmodeId=%s, \n\tcompanyListId=%s, \n\tindustryCompanyListId=%s, \n\tyear=%s",
                    modelId, companyListId, industryCompanyListId, year));
            String result = dataService.performCalculation(modelId, companyListId, industryCompanyListId, year);
            return ResponseHelper.successResponse(result);
        } catch (Exception e) {
            throw new RestException(e);
        }
    }

}