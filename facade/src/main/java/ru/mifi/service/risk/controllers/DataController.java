package main.java.ru.mifi.service.risk.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import main.java.ru.mifi.service.risk.exception.RestException;

import java.util.Map;

@RestController
@RequestMapping(value = "/testController")
@Api(value = "Тестовые контроллер", description = "Описание тестового контроллера")
public class DataController extends ExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(DataController.class);


    @ApiOperation(value = "Тестовая операция")
    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> persist(
            @ApiParam(value = "Тестовый параметр")
            @RequestParam("data") String data
    ) throws RestException {
        try {
            if (data == null || data.equals("")) {
                return ResponseHelper.emptyResponse();
            }
            return ResponseHelper.successResponse(data);
        } catch (Exception e) {
            throw new RestException(e);
        }
    }
}