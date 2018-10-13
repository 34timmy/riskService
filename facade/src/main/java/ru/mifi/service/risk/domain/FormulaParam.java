package ru.mifi.service.risk.domain;

/**
 * Инкапсулирует работу с _с,_t,_p
 * Created by DenRUS on 13.10.2018.
 */
public class FormulaParam {
    private Shift yearShift;

    public Integer getYearShift() {
        return yearShift.getShiftVal();
    }

    public String getParamCode() {
        return paramCode;
    }

    private String paramCode;

    public FormulaParam(String param) {
        int length = param.length();
        this.paramCode = param.substring(1, length - 2);
        this.yearShift = Shift.valueOf(param.substring(length-2, length));
    }

    private enum Shift {
        _c(0),
        _p(-1),
        _t(-2);

        public Integer getShiftVal() {
            return shiftVal;
        }

        Shift(Integer shiftVal) {
            this.shiftVal = shiftVal;
        }

        private Integer shiftVal;
    }
}
