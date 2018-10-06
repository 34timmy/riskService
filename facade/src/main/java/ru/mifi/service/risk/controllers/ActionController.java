package ru.mifi.service.risk.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mifi.service.risk.exception.RestException;
import ru.mifi.service.risk.utils.DataService;
import ru.mifi.service.risk.utils.ExcelLoader;

import java.util.Map;

@RestController
@RequestMapping(value = "/perform")
@Api(value = "Контроллер для осуществления основных действий", description = "Запускает расчет и т.д.")
public class ActionController extends ExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(ActionController.class);

    @ApiOperation(value = "Запустить основной расчет")
    @RequestMapping(value = "/calculation", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> calculate(
            @ApiParam(value = "Id модели")
            @RequestParam("modelId") String modelId,
            @ApiParam(value = "Id списка компаний")
            @RequestParam("companyListId") String companyListId
    ) throws RestException {
        try {
            String result = new DataService().performCalculation(modelId, companyListId);
            return ResponseHelper.successResponse(result);
        } catch (Exception e) {
            throw new RestException(e);
        }
    }

}