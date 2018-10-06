package ru.mifi.service.risk.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.mifi.service.risk.exception.RestException;
import ru.mifi.service.risk.utils.ExcelLoader;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Map;

@RestController
@RequestMapping(value = "/import")
@Api(value = "Контроллер для иморта данных в БД", description = "Включает методы для загрузки данных в БД")
public class ImportController extends ExceptionHandlerController {

    private static final Logger LOG = LoggerFactory.getLogger(ImportController.class);
    private DataSource dataSource;
    @Resource(name="dataSource")
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @ApiOperation(value = "Из экселя")
    @RequestMapping(value = "/fromExcel", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> fromExcel(
            @ApiParam(value = "Файл")
            @RequestParam("file") MultipartFile file
    ) throws RestException {
        try {
            String responseMsg = new ExcelLoader(dataSource).loadExcelFile(file.getInputStream());
            return ResponseHelper.successResponse(responseMsg);
        } catch (Exception e) {
            throw new RestException(e);
        }
    }

}