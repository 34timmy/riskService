package ru.mifi.service.risk.utils;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mifi.service.risk.database.DatabaseExcelImportAccessor;
import ru.mifi.service.risk.exception.DatabaseException;
import ru.mifi.service.risk.exception.ImportException;
import ru.mifi.service.risk.utils.validation.ValidationUtil;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * Загружаем данные о модели и(или) списке компаний из Excel-файла в базу
 * Created by DenRUS on 06.10.2018.
 */
public class ExcelLoader {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelLoader.class);
    private static final DataFormatter formatter = new DataFormatter();

    private final DataSource ds;
    public ExcelLoader(DataSource dataSource) {
        this.ds = dataSource;
    }


    /**
     * Загружаем данные по Excel-файлу в базу
     * @param inputStream   входной файл как стрим
     * @return  сообщение о результатах загрузки
     */
    public String loadExcelFile(InputStream inputStream) {
        XSSFWorkbook workbook;

        try {
            workbook = new XSSFWorkbook(inputStream);

            loadData(workbook.getSheet("data"));

            loadFormulas(workbook.getSheet("formulas"));


            LOG.info("Файл успешно обработан.");
        } catch (IOException e) {
            throw new ImportException("Ошибка при обработке файла: " + e.getMessage());
        }
        return "Всё ок";
    }

    /**
     * Обработка листа с данными
     *
     * @param sheet лист с данными
     */
    private void loadData(XSSFSheet sheet) {
        try (DatabaseExcelImportAccessor accessor = new DatabaseExcelImportAccessor(ds)) {
            XSSFRow rowNames = sheet.getRow(0);
            ValidationUtil.sheetCheck(sheet);

            for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                Row row = sheet.getRow(r);
                if (row == null || formatter.formatCellValue(row.getCell(0)).equalsIgnoreCase("") ||
                        formatter.formatCellValue(row.getCell(1)).equalsIgnoreCase("")) {
                    continue;
                }

                String inn = formatter.formatCellValue(row.getCell(0));
                Integer year = Integer.valueOf(formatter.formatCellValue(row.getCell(1)));
                accessor.insertCompany(inn, inn);

                for (int column = 2; column < row.getLastCellNum(); column++) {
                    String value = formatter.formatCellValue(row.getCell(column)).replaceAll(",", ".").trim();
                    value = ValidationUtil.fieldCheck(formatter.formatCellValue(rowNames.getCell(column)).trim(), value);
                    String paramCode = formatter.formatCellValue(rowNames.getCell(column)).toUpperCase().trim();
                    accessor.insertCompanyParam(inn, paramCode, year, Double.parseDouble(value));
                }

            }
        } catch (SQLException e) {
            throw new DatabaseException("Ошибка при работе с БД", e);
        } catch (Exception e) {
            throw new ImportException("Ошибка при работе импорте данных", e);
        }
    }

    /**
     * Обработка листа с формулами
     */
    private void loadFormulas(XSSFSheet sheet) {
//        formulas = new HashSet<>();
//        ValidationUtil.sheetCheck(sheet);
//        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
//            Row row = sheet.getRow(r);
//            int column = 0;
//
//            try {
//                if (row.getCell(0) != null && !row.getCell(0).toString().equals("") && formulaIsInAnalyse(formatCellVal(row.getCell(17)))) {
//
//                    Formula formula = new Formula(
//                            formatCellVal(row.getCell(0)),                //id
//                            formatCellVal(row.getCell(1)),                //descr
//                            editInput(formatCellVal(row.getCell(2))),                   //calculation
//                            formatCellVal(row.getCell(3)),                              //formula_type
//                            formatCellVal(row.getCell(4)),                              //A
//                            formatCellVal(row.getCell(5)),                              //B
//                            formatCellVal(row.getCell(6)),                              //C
//                            formatCellVal(row.getCell(7)),                              //D
//                            formatCellVal(row.getCell(9)),                              //_XB
//                            Double.valueOf(formatCellVal(row.getCell(11))               //Weight
//                            ));
//                    Set<String> comments = new LinkedHashSet<>();
//                    for (int i = 12; i <= 16; i++) {
//                        if (formatter.formatCellValue(row.getCell(i)) != null &&
//                                !formatter.formatCellValue(row.getCell(i)).equalsIgnoreCase("")) {
//                            comments.add(formatter.formatCellValue(row.getCell(i)));
//                        }
//                    }
//                    formula.setComments(comments);
//                    formulaDataCheck(formula);
//                    formulas.add(formula);
//                }
//
//            } catch (WrongFormulaValueException e) {
//                SystemLogger.error("Ошибка при загрузке формул", e);
//            } catch (NullPointerException ex) {
//                throw new WrongFormulaValueException("Ошибка загрузки формулы на строке " + r + "." + ex);
//            }

//        }
    }
}
