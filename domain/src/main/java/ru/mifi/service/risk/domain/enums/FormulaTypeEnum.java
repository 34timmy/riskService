package ru.mifi.service.risk.domain.enums;


import ru.mifi.service.risk.domain.Formula;
import ru.mifi.service.risk.exception.WrongFormulaValueException;
import ru.mifi.service.risk.utils.validation.ValidationUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Перечисление возможных типов нечетких формул
 *
 * @author DenRUS on 22.09.2017
 */
public enum FormulaTypeEnum {

    S(1) {
        @Override
        public double calculate(double input, double A, double B, double C, double D,
                                double _XB) throws WrongFormulaValueException {

            A = B - _XB;
            C = B + _XB;
            if (input <= A) {
                return 0D;
            } else if (input >= A && input <= B) {
                return 2 * Math.pow(((input - A) / ValidationUtil.divisionByZeroCheck(C - A)), 2);
            } else if (input >= B && input <= C) {
                return 1 - 2 * Math.pow(((input - C) / ValidationUtil.divisionByZeroCheck(C - A)), 2);
            } else if (input >= C) {
                return 1D;
            }
            return 0.0;
        }
    },
    SB(2) {
        @Override
        public double calculate(double input, double A, double B, double C, double D,
                                double _XB) throws WrongFormulaValueException {

            A = 0D;
            B = 0.5D;
            C = 1D;
            if (input <= A) {
                return 0D;
            } else if (input >= A && input <= B) {
                return 2 * Math.pow(((input - A) / ValidationUtil.divisionByZeroCheck(C - A)), 2);
            } else if (input >= B && input <= C) {
                return 1 - 2 * Math.pow(((input - C) / ValidationUtil.divisionByZeroCheck(C - A)), 2);
            } else if (input >= C) {
                return 1D;
            }
            return 0.0;
        }
    },
    Z(3) {
        @Override
        public double calculate(double input, double A, double B, double C, double D,
                                double _XB) throws WrongFormulaValueException {
            return 1 - FormulaTypeEnum.S.calculate(input, A, B, C, D, _XB);
        }
    },
    //    Трапеция
    T(4) {
        @Override
        public double calculate(double input, double A, double B, double C, double D,
                                double _XB) throws WrongFormulaValueException {
            if (input <= A) {
                return 0D;
            } else if (input >= A && input <= B) {
                return ((input - A) / ValidationUtil.divisionByZeroCheck(B - A));
            } else if (input >= B && input <= C) {
                return 1D;
            } else if (input >= C && input <= D) {
                return ((D - input) / ValidationUtil.divisionByZeroCheck(D - C));

            } else if (input >= D) {

                return 0D;
            }
            return 0.0;
        }

    },
    //    Обратная трапеция
    TR(5) {
        @Override
        public double calculate(double input, double A, double B, double C, double D,
                                double _XB) throws WrongFormulaValueException {
            return 1 - FormulaTypeEnum.T.calculate(input, A, B, C, D, _XB);
        }

    },
    //    Ступенчатая трапеция
    STT(6) {
        @Override
        public double calculate(double input, double A, double B, double C, double D,
                                double _XB) throws WrongFormulaValueException {

            ValidationUtil.checkParams(A, B, C, D);

            if (input <= A) {
                return 0D;
            } else if (input >= A && input <= B) {
                return (input - A) / ValidationUtil.divisionByZeroCheck(2 * (B - A));
            } else if (input >= B && input <= C) {
                return 0.5D;
            } else if (input >= C && input <= D) {
                return ((input + D - 2 * C) / ValidationUtil.divisionByZeroCheck(2 * (D - C)));

            } else if (input >= D) {

                return 1D;
            }

            return 0.0;
        }

    },
    //    Ступенчатая трапеция обратная
    STTR(7) {
        @Override
        public double calculate(double input, double A, double B, double C, double D,
                                double _XB) throws WrongFormulaValueException {

            ValidationUtil.checkParams(A, B, C, D);

            return 1 - FormulaTypeEnum.STT.calculate(input, A, B, C, D, _XB);
        }

    },
    SA(8) {
        @Override
        public double calculate(double input, double A, double B, double C, double D, double _XB) throws WrongFormulaValueException {
            return FormulaTypeEnum.S.calculate(input, A, B, C, D, _XB);
        }

        @Override
        public Map<String, String> getParameters(String calculationFormula, String a, String b, String c, String d, String _XB) {
            return new HashMap<String, String>() {{
                put(Formula.A_NAME, a);
                put(Formula.B_NAME, "MED(" + calculationFormula + ")END");
                put(Formula.C_NAME, c);
                put(Formula.D_NAME, d);
                put(Formula._XB_NAME, "MIN{MED(" + calculationFormula + ")END- MIN(" + calculationFormula + ")END;MAX(" + calculationFormula +
                        ")END-MED(" + calculationFormula + ")END}");
                put(Formula.CALCULATION_INPUT_NAME, calculationFormula);
            }};
        }
    },
    ZA(9) {
        @Override
        public double calculate(double input, double A, double B, double C, double D, double _XB) throws WrongFormulaValueException {
            return FormulaTypeEnum.Z.calculate(input, A, B, C, D, _XB);
        }

        @Override
        public Map<String, String> getParameters(String calculationFormula, String a, String b, String c, String d, String _XB) {
            return FormulaTypeEnum.SA.getParameters(calculationFormula, a, b, c, d, _XB);
        }
    };
    private int num;

    FormulaTypeEnum(int num) {
        this.num = num;
    }


    public int getNum() {
        return num;
    }

    /**
     * Метод расчета риска конкретной формулой. Переопределяется каждым типом по своему
     *
     * @param input входное значение
     * @param A     параметр А формулы
     * @param B     параметр b формулы
     * @param C     параметр c формулы
     * @param D     параметр d формулы
     * @param _XB   параметр xb формулы
     * @return значение расчета формулы (риск)
     * @throws WrongFormulaValueException При неверной формуле
     */
    public abstract double calculate(double input, double A, double B, double C, double D,
                                     double _XB) throws WrongFormulaValueException;

    /**
     * Получить параметры в виде мапы
     *
     * @param calculationFormula входное значение
     * @param a                  параметр а
     * @param b                  параметр b
     * @param c                  параметр c
     * @param d                  параметр d
     * @param _XB                параметр xb
     * @return мапа с параметрами
     */
    public Map<String, String> getParameters(String calculationFormula, String a, String b, String c, String d, String _XB) {
        return new HashMap<String, String>() {{
            put(Formula.A_NAME, a);
            put(Formula.B_NAME, b);
            put(Formula.C_NAME, c);
            put(Formula.D_NAME, d);
            put(Formula._XB_NAME, _XB);
            put(Formula.CALCULATION_INPUT_NAME, calculationFormula);
        }};
    }
}
