package ru.mifi.service.risk.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.mifi.service.risk.database.DatabaseSelectAccessor;
import ru.mifi.service.risk.domain.CalculationParamKey;
import ru.mifi.service.risk.dto.CalcResultDto;
import ru.mifi.service.risk.exception.RestException;
import ru.mifi.service.risk.utils.DataService;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;

import static ru.mifi.service.risk.database.DatabaseDdlAccessor.getTableNameByDataKey;

@RestController
@RequestMapping(value = "/export")
@Api(value = "Контроллер для экспорта (выгрузки) данных в БД", description = "Включает методы для выгрузки данных расчета")
public class ExportDataController extends ExceptionHandlerController {

    @Autowired
    private DatabaseSelectAccessor accessor;

    @Autowired
    private DataService service;

    private static final Logger LOG = LoggerFactory.getLogger(ExportDataController.class);

    @ApiOperation(value = "В эксел")
    @RequestMapping(value = "/toExcel", method = RequestMethod.POST)
    public @ResponseBody
    ResponseEntity<byte[]> toExcel(
            @ApiParam(value = "Куда сохранять")
            @RequestParam("file") MultipartFile file
    ) throws RestException {
        //TODO:
        return null;
    }

    @ApiOperation(value = "В json (по имени таблицы)")
    @RequestMapping(value = "/toJsonByTable", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @SneakyThrows
    @ResponseBody
    public ResponseEntity<InputStreamResource> toJsonByTable(
            @ApiParam(value = "Имя таблицы с данными")
            @RequestParam("tableName") String tableName
    ) throws RestException {
        byte[] buf = new JSONObject(accessor.getDataFromTable(tableName)).toString().getBytes();
        return ResponseEntity
                .ok()
                .contentLength(buf.length)
                .header("Content-Disposition", "attachment; filename=\"" + tableName + ".json\"")
                .contentType(
                        MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(new ByteArrayInputStream(buf)));
    }

    @ApiOperation(value = "В json (по параметрам расчета - последний результат)")
    @RequestMapping(value = "/toJsonByParams", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @SneakyThrows
    @ResponseBody
    public ResponseEntity<InputStreamResource> toJsonByParams(
            @ApiParam(value = "Id модели")
            @RequestParam("modelId") String modelId,
            @ApiParam(value = "Id списка компаний")
            @RequestParam("companyListId") String companyListId,
            @ApiParam(value = "Id списка компаний всей отрасли")
            @RequestParam("industryCompanyListId") String industryCompanyListId,
            @ApiParam(value = "Год для расчета")
            @RequestParam("year") Integer year
    ) throws RestException {
        CalculationParamKey curParams = new CalculationParamKey(modelId, companyListId, industryCompanyListId, year);
        LOG.info("Получен запрос на экспорт в JSON данных расчета: " + curParams);
        Map<String, CalcResultDto> result = service.getCalculationResultFromDb(curParams);
        byte[] buf = new JSONObject(result).toString().getBytes();
        return ResponseEntity
                .ok()
                .contentLength(buf.length)
                .header("Content-Disposition", "attachment; filename=\"" + getTableNameByDataKey(curParams) + ".json\"")
                .contentType(
                        MediaType.parseMediaType("application/octet-stream"))
                .body(new InputStreamResource(new ByteArrayInputStream(buf)));
    }
}