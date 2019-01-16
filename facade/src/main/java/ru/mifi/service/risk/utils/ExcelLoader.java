package ru.mifi.service.risk.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mifi.service.risk.database.DatabaseExcelImportAccessor;
import ru.mifi.service.risk.domain.FormulaParam;
import ru.mifi.service.risk.exception.DatabaseException;
import ru.mifi.service.risk.exception.ImportException;
import ru.mifi.service.risk.exception.WrongFormulaValueException;
import ru.mifi.service.risk.utils.type.ParamsTypeEnum;
import ru.mifi.service.risk.utils.validation.ValidationUtil;
import ru.mifi.utils.StringUtils;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;
import java.util.UUID;

/**
 * Загружаем данные о модели и(или) списке компаний из Excel-файла в базу
 * Created by DenRUS on 06.10.2018.
 */
@Component
public class ExcelLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelLoader.class);
    private static final DataFormatter formatter = new DataFormatter();

    private final DataSource ds;

    @Autowired
    public ExcelLoader(DataSource dataSource) {
        this.ds = dataSource;
    }


    /**
     * Загружаем данные по Excel-файлу в базу
     *
     * @param inputStream входной файл как стрим
     * @return сообщение о результатах загрузки
     */
    public Map<String, Object> loadExcelFile(InputStream inputStream) {
        XSSFWorkbook workbook;

        try (DatabaseExcelImportAccessor accessor = new DatabaseExcelImportAccessor(ds)) {
            workbook = new XSSFWorkbook(inputStream);

            loadData(workbook.getSheet("data"), accessor);

            String modelId = loadModel(workbook.getSheet("model"), accessor);

            loadFormulas(workbook.getSheet("formulas"), accessor);


            LOG.info("Файл успешно обработан.");
            return returnResultAsMap(modelId);
        } catch (IOException e) {
            throw new ImportException("Ошибка при обработке файла: " + e.getMessage());
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при работе с БД: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new ImportException("Ошибка при работе импорте данных", e);
        }
    }

    private Map<String, Object> returnResultAsMap(String modelId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("message", "Документ успешно загружен. ");
        resultMap.put("modelId", modelId);
        return resultMap;
    }

    /**
     * Загружаем иерархию модели
     *
     * @param modelSheet лист с моделью
     * @param accessor   объект, сохраняющий данные в бд
     * @throws SQLException если косяк при работе с БД
     */
    private String loadModel(XSSFSheet modelSheet, DatabaseExcelImportAccessor accessor) throws SQLException {
        String modelId = UUID.randomUUID().toString();
        accessor.insertModel(modelId);
        ValidationUtil.sheetCheck(modelSheet);
        for (int r = 1; r <= modelSheet.getLastRowNum(); r++) {
            Row row = modelSheet.getRow(r);
            String nodeId = formatCellVal(row.getCell(0));
            String descr = formatCellVal(row.getCell(1));
            String parentNode = getNullableCellValue(row.getCell(2));
            Double weight = Double.valueOf(formatCellVal(row.getCell(3)));               //Weight
            Integer level = Integer.parseInt(formatCellVal(row.getCell(4)));
            Integer isLeaf = Integer.parseInt(formatCellVal(row.getCell(5)));
            Double interpreteK = Double.parseDouble(formatCellVal(row.getCell(6)));
            StringJoiner comments = new StringJoiner(";");
            for (int i = 7; i <= 9; i++) {
                if (formatter.formatCellValue(row.getCell(i)) != null &&
                        !formatter.formatCellValue(row.getCell(i)).equalsIgnoreCase("")) {
                    comments.add(formatter.formatCellValue(row.getCell(i)));
                }
            }
            accessor.insertModelCalc(
                    nodeId,
                    modelId,
                    descr,
                    parentNode,
                    weight,
                    level,
                    isLeaf,
                    interpreteK,
                    comments.toString()
            );
        }
        return modelId;
    }

    /**
     * Обработка листа с данными
     *
     * @param sheet лист с данными
     */
    private void loadData(XSSFSheet sheet, DatabaseExcelImportAccessor accessor) throws SQLException {
        XSSFRow rowNames = sheet.getRow(0);
        ValidationUtil.sheetCheck(sheet);
        String prevInn = null;

        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            if (row == null || formatter.formatCellValue(row.getCell(0)).equalsIgnoreCase("") ||
                    formatter.formatCellValue(row.getCell(1)).equalsIgnoreCase("")) {
                continue;
            }

            String inn = formatter.formatCellValue(row.getCell(0));
            Integer year = Integer.valueOf(formatter.formatCellValue(row.getCell(1)));
            if (!inn.equals(prevInn)) {
                accessor.insertCompany(inn, inn);
                prevInn = inn;
            }

            for (int column = 2; column < row.getLastCellNum(); column++) {
                String value = formatter.formatCellValue(row.getCell(column)).replaceAll(",", ".").trim();
                value = ValidationUtil.fieldCheck(formatter.formatCellValue(rowNames.getCell(column)).trim(), value);
                String paramCode = formatter.formatCellValue(rowNames.getCell(column)).toUpperCase().trim();
                accessor.insertCompanyParam(inn, paramCode, year, Double.parseDouble(value));
            }

        }
    }

    /**
     * Обработка листа с формулами
     */
    private void loadFormulas(XSSFSheet sheet, DatabaseExcelImportAccessor accessor) throws SQLException {
        ValidationUtil.sheetCheck(sheet);
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            Row row = sheet.getRow(r);
            try {
                if (row.getCell(0) != null && !row.getCell(0).toString().equals("")) {

                    String calculation = editInput(formatCellVal(row.getCell(2)));  //calculation
                    String nodeId = formatCellVal(row.getCell(0));                  //id
                    Set<String> allParams = new HashSet<>();
                    String parentNode = getParentNode(nodeId);

                    accessor.insertFormula(
                            nodeId,
                            formatCellVal(row.getCell(1)),                              //name
                            calculation,                                                   //calculation
                            formatCellVal(row.getCell(3)),                              //formula_type
                            formatCellVal(row.getCell(4)),                              //A
                            formatCellVal(row.getCell(5)),                              //B
                            formatCellVal(row.getCell(6)),                              //C
                            formatCellVal(row.getCell(7)),                              //D
                            formatCellVal(row.getCell(8))                             //_XB
                    );
                    allParams.addAll(ParamsTypeEnum.SB.parseCalculationToParams(calculation));
                    allParams.addAll(ParamsTypeEnum.SB.parseCalculationToParams(formatCellVal(row.getCell(8))));
                    allParams.addAll(ParamsTypeEnum.SB.parseCalculationToParams(formatCellVal(row.getCell(4))));
                    allParams.addAll(ParamsTypeEnum.SB.parseCalculationToParams(formatCellVal(row.getCell(5))));
                    allParams.addAll(ParamsTypeEnum.SB.parseCalculationToParams(formatCellVal(row.getCell(6))));
                    allParams.addAll(ParamsTypeEnum.SB.parseCalculationToParams(formatCellVal(row.getCell(7))));
                    parseAndSaveParams(nodeId, allParams, accessor);

                }
            } catch (WrongFormulaValueException e) {
                LOG.error("Ошибка при загрузке формул", e);
            } catch (NullPointerException ex) {
                throw new WrongFormulaValueException("Ошибка загрузки формулы на строке " + r + "." + ex);
            }

        }
    }

    private void parseAndSaveParams(String nodeId, Collection<String> allParams, DatabaseExcelImportAccessor accessor) {
        allParams.stream()
                .map(FormulaParam::new)
                .forEach(elem -> {
                    try {
                        accessor.insertFormulaParam(nodeId, elem.getParamCode(), elem.getYearShift());
                    } catch (SQLException e) {
                        throw new DatabaseException("Ошибка при импорте параметра формулы: " + e.getMessage(), e);
                    }
                });
    }

    /**
     * Определяем есть ли в идентификаторе родительский узел.
     *
     * @param nodeId узел
     * @return родительский узел
     */
    private String getParentNode(String nodeId) {
        int position = nodeId.lastIndexOf(".");
        return position == -1
                ? null
                : nodeId.substring(0, position);
    }

    private String getNullableCellValue(Cell cell) {
        String value = formatter.formatCellValue(cell).toUpperCase();
        if (StringUtils.isNullOrEmpty(value)) {
            return null;
        }
        return value;
    }

    private String formatCellVal(Cell cell) {
        String value = formatter.formatCellValue(cell).toUpperCase();
        if (value.equalsIgnoreCase("") || value.equalsIgnoreCase("NaN")) {
            value = "0";
        }
        return value;
    }

    /**
     * Форматируем входную формулу - удаляем лишние пробелы, энтеры и т.д.
     *
     * @param formula входная формула
     * @return отредактированная формула
     */
    private String editInput(String formula) {
        return formula.replaceAll("[ \n\t]", "").toUpperCase();
    }
}
