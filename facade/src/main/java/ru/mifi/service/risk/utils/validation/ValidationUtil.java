package ru.mifi.service.risk.utils.validation;


import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mifi.service.risk.exception.WrongFormulaValueException;

import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс валидации данных
 * TODO: отрефакторить,если надо
 *
 * @author Timur
 */
public class ValidationUtil {

    private static final Logger LOG = LoggerFactory.getLogger(ValidationUtil.class);

    /**
     * Проверка значения СБ из экселя
     *
     * @param rowName - Информация для логов
     * @param value   - Проверяемое значение
     * @return - Если value не число, то присваиваем 0, иначе возвращаем само число.
     */
    public static String fieldCheck(String rowName, String value) {
        Pattern pattern = Pattern.compile("[-]?\\d+(.?|,?)\\d*");
        Matcher matcher = pattern.matcher(value);
        if (!matcher.matches()) {
            LOG.info(String.format(
                    "Строка баланса = %s.\n Значение '%s' не корректно.",
                     rowName, value));
            value = Double.NaN+"";
        }
        return value;
    }


    /**
     * Коррекция формулы для верных расчетов
     * @param formula   формула
     * @return          корректированная формула
     */
    public static String inputCorrection(String formula) {

        return formula
                .replaceAll(" ", "")
                .replaceAll("\n", "")
                .replaceAll("\t", "")
                .replaceAll("С", "C")
                .replaceAll("--", "+")
                .replaceAll("\\+-", "-")
                .replaceAll("-\\+", "-")
                .replaceAll("\\+\\+", "+")
                .replaceAll(",", ".")
                ;
    }

    /**
     * Проверка параметров формулы
     * @param A параметр А
     * @param B параметр B
     * @param C параметр C
     * @param D параметр D
     * @throws WrongFormulaValueException неверная формула
     */
    public static void checkParams(double A, double B, double C, double D) throws WrongFormulaValueException {
        if (!(A <= B && B <= C && C <= D)) {
            throw new WrongFormulaValueException("Не выполняется улсовие: A<=B<=C<=D");
        }
    }

    /**
     * Проверка количества скобок в формуле.
     *
     * @param formula - Формула
     * @throws WrongFormulaValueException - Кидается при нечетных скобках.
     */
    public static void formulaBracketsCheck(String formula) throws WrongFormulaValueException {


        Map<Integer, Character> map = new TreeMap<>();
        int leng = 0;
        int count = 0;
        for (char c : formula.toCharArray()) {
            leng++;
            if (c == '(') {
                map.put(leng, c);
                count++;
            } else if (c == ')') {
                map.put(leng, c);
                count--;
            }
        }
        if (count > 0) {
            LOG.warn(String.format("Не валидная формула = %s. Не хватает закрывающей скобки ')'.", formula));
            throw new WrongFormulaValueException("Не хватает закрывающей скобки ')'");
        } else if (count < 0) {
            LOG.warn(String.format("Не валидная формула = %s. Не хватает открывающей скобки '('.", formula));
            throw new WrongFormulaValueException("Не хватает открывающей скобки '('");
        }

    }

    /**
     * Проверка деления на ноль
     *
     * @param denominator - Выражение в знаменателе
     * @return значение, если все хорошо
     */
    public static Double divisionByZeroCheck(Double denominator) {
        if (denominator.equals(0D)) {
            throw new IllegalArgumentException("Деление на ноль!");
        }
        return denominator;
    }

    /**
     * Проверка данных на листе.
     *
     * @param sheet - Лист с данными.
     */
    public static void sheetCheck(XSSFSheet sheet) {
        if (sheet.getLastRowNum() <= 0) {
            throw new RuntimeException("Нет данных на листе - " + sheet.getSheetName());
        }
    }
}
