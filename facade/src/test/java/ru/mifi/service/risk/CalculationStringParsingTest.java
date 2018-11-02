package ru.mifi.service.risk;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import ru.mifi.service.risk.utils.params.type.ParamsTypeEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Тестик
 * Created by DenRUS on 02.11.2018.
 */
@RunWith(JUnit4.class)
public class CalculationStringParsingTest {

    @Test
    public void calculationParsingTest() {
        String calculationForTest = "(SB_1600_c-SB_1500_c-SB_1400_c+SB_1530_c-SB_1600_p+SB_1500_p+SB_1400_p-SB_1530_p)/(SB_1600_p-SB_1500_p-SB_1400_p+SB_1530_p)*100";
        List<String> result = ParamsTypeEnum.SB.parseCalculationToParams(calculationForTest);
        List<String> correctResult = Arrays.stream("SB_1600_c;SB_1500_c;SB_1400_c;SB_1530_c;SB_1600_p;SB_1500_p;SB_1400_p;SB_1530_p".toUpperCase().split(";")).collect(Collectors.toList());
        Assert.assertEquals(result, correctResult);

    }
}
