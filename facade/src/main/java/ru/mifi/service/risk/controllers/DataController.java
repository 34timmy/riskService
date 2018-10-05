package ru.mifi.service.risk.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mifi.service.risk.exception.RestException;

import java.util.Map;

@RestController
@RequestMapping(value = "/testController")
@Api(value = "Тестовые контроллер", description = "Описание тестового контроллера")
public class DataController extends ExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(DataController.class);


    @ApiOperation(value = "Тестовая операция")
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> persist(
            @ApiParam(value = "Тестовый параметр")
            @RequestParam(value = "data", required = false) String data
    ) throws RestException {
        try {
            if (data == null || data.equals("")) {
                return ResponseHelper.emptyResponse();
            }
            LOG.info("test-info");
            LOG.warn("test-warn");
            return ResponseHelper.successResponse(data);
        } catch (Exception e) {
            throw new RestException(e);
        }
    }
}