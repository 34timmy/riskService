package ru.mifi.service.risk.export.excel;

import lombok.SneakyThrows;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Component;
import ru.mifi.service.risk.dto.CalcResultDto;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Инкапсуляция логики выгрузки данных в Excel.
 * <p>
 * Created by DenRUS on 10.11.2018.
 */
@Component
public class ExcelExporter {

    private static final short HEADER_ROW_HEIGHT = 1000;
    private static final String DEFAULT_EMPTY_VALUE = " ";

    private static final Map<String, Integer> TABLE_HEADER_MAP = new LinkedHashMap<>();

    static {
        TABLE_HEADER_MAP.put("ID компании (ИНН)", 8000);
        TABLE_HEADER_MAP.put("ID ноды-родителя", 4000);
        TABLE_HEADER_MAP.put("ID текущей ноды", 4000);
        TABLE_HEADER_MAP.put("Вес ноды", 4000);
        TABLE_HEADER_MAP.put("Флаг листа", 4000);
        TABLE_HEADER_MAP.put("Нормированный результат", 6000);
        TABLE_HEADER_MAP.put("Результат", 6000);
        TABLE_HEADER_MAP.put("Комментарий", 35000);
    }

    private CellStyle HEADER_STYLE;
    private CellStyle DATA_STYLE;
    private int nextRowNum = 0;

    /**
     * Записываем результат иерархии в excel и отдаем как массив байт
     *
     * @param resultData результат расчета иерархии
     * @return excel-файл как массив байт
     */
    public byte[] exportHierarchyResult(Map<String, CalcResultDto> resultData, String tableName) {
        Workbook workbook = new HSSFWorkbook();
        HEADER_STYLE = createHeaderStyle(workbook);
        DATA_STYLE = createDataStyle(workbook);
        Sheet activeSheet = workbook.createSheet("Результат расчета");
        Row curRow = activeSheet.createRow(nextRowNum++);
        addHeaderRegion(activeSheet, nextRowNum - 1, "Результат расчета иерархии из таблицы: " + tableName);


        setColumnsWidth(activeSheet);

        activeSheet.createRow(nextRowNum++);

        createTableHeaderRow(activeSheet);

        writeData(resultData, activeSheet);

        return getWorkBookAsByteArray(workbook);
    }

    /**
     * Добавляем заголовок, объединяющий несколько строк
     * @param activeSheet активный лист Excel
     * @param rowNum номер строки с этим заголовком
     * @param headerValue содержание заголовка
     */
    private void addHeaderRegion(Sheet activeSheet, int rowNum, String headerValue) {
        int columnsCount = TABLE_HEADER_MAP.keySet().size() - 1;
        Row activeRow = activeSheet.getRow(rowNum);
        addHeaderCell(activeRow, 0).setCellValue(headerValue);
        for (int i = 1; i <= columnsCount; i++) {
            addHeaderCell(activeRow, i);
        }
        activeSheet.addMergedRegion(new CellRangeAddress(
                rowNum,
                rowNum,
                rowNum,
                columnsCount
        ));
    }

    /**
     * Устанавливаем нужную ширину ячеек для столбцов таблицы.
     *
     * @param activeSheet активный лист Excel
     */
    private void setColumnsWidth(Sheet activeSheet) {
        short columnNum = 0;
        for (Integer width : TABLE_HEADER_MAP.values()) {
            activeSheet.setColumnWidth(columnNum++, width);
        }
    }

    /**
     * Записываем целиком результаты расчета в Excel
     *
     * @param resultData  результаты расчета
     * @param activeSheet активный лист Excel
     */
    private void writeData(Map<String, CalcResultDto> resultData, Sheet activeSheet) {
        Collection<CalcResultDto> dtos = resultData.values();
        for (CalcResultDto dto : dtos) {
            writeDtoData(dto, activeSheet);
        }
    }

    /**
     * Записываем результаты одного DTO (узла и всех его подузлов) в Excel.
     *
     * @param dto         узел(нода)
     * @param activeSheet активный лист Excel
     */
    private void writeDtoData(CalcResultDto dto, Sheet activeSheet) {
        Row curRow = activeSheet.createRow(nextRowNum++);
        setNextCellValue(curRow, dto.getCompanyId());
        setNextCellValue(curRow, dto.getParentNode());
        setNextCellValue(curRow, dto.getNode());
        setNextCellValue(curRow, dto.getWeight());
        setNextCellValue(curRow, dto.getIsLeaf());
        setNextCellValue(curRow, dto.getNormalizedValue());
        setNextCellValue(curRow, dto.getValue());
        setNextCellValue(curRow, dto.getComment());
        Set<CalcResultDto> children = dto.getChildren();
        if (children != null && children.size() > 0) {
            children.forEach(childDto -> writeDtoData(childDto, activeSheet));
        }

    }

    /**
     * Создаем следующую по счету ячейку в строке и записываем туда значение
     * (либо значение по умолчанию, если value = null).
     *
     * @param curRow текущая строка для работы
     * @param value  значение для записи
     */
    private void setNextCellValue(Row curRow, Object value) {
        int lastCellNum = curRow.getLastCellNum();
        Cell newCell = setDataCellStyle(curRow.createCell(
                lastCellNum == -1
                        ? 0
                        : lastCellNum
        ));
        if (value == null) {
            newCell.setCellValue(DEFAULT_EMPTY_VALUE);
            return;
        }
        if (value instanceof Double) {
            newCell.setCellValue((Double) value);
        }
        if (value instanceof Integer) {
            newCell.setCellValue((Integer) value);
        }
        newCell.setCellValue(value.toString());
    }

    /**
     * Создает строку с хидерами таблицы результатов
     *
     * @param activeSheet лист экселя
     */
    private void createTableHeaderRow(Sheet activeSheet) {
        Row headerRow = activeSheet.createRow(nextRowNum++);
        int columnNum = 0;
        for (String header : TABLE_HEADER_MAP.keySet()) {
            Cell headerCell = headerRow.createCell(columnNum++);
            headerCell.setCellStyle(HEADER_STYLE);
            headerCell.setCellValue(header);
        }

    }

    /**
     * Задаем ячейке стиль для ячеек с данными
     *
     * @param cell ячейка для работы
     * @return эта же ячейка
     */
    private Cell setDataCellStyle(Cell cell) {
        cell.setCellStyle(DATA_STYLE);
        return cell;
    }

    /**
     * Создаем шрифт для заголовков
     *
     * @param workbook книга Excel для работы
     * @return фон для заголовков
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontName("Times New Roman");
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setFont(font);

        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        return cellStyle;
    }

    /**
     * Создаем шрифт для ячеек с данными
     *
     * @param workbook книга Excel для работы
     * @return фон для данных
     */
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(false);
        font.setFontName("Times New Roman");
        cellStyle.setFont(font);

        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);

        return cellStyle;
    }

    /**
     * Добавляет следующу ячейку как ячейку заголовка листа.
     *
     * @param curRow текущая строка
     * @param column номер столбца для добавления ячейки
     * @return ячейка-заголовок
     */
    private Cell addHeaderCell(Row curRow, Integer column) {
        curRow.setHeight(HEADER_ROW_HEIGHT);
        Cell resultCell = curRow.createCell(column);
        resultCell.setCellStyle(HEADER_STYLE);
        return resultCell;
    }

    /**
     * Просто отдает workbook как массив байт
     *
     * @param workbook workbook excel`я для выдачи
     * @return массив байт
     */
    @SneakyThrows
    private byte[] getWorkBookAsByteArray(Workbook workbook) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        return out.toByteArray();
    }
}
