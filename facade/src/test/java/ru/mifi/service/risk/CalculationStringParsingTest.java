package ru.mifi.service.risk;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.runner.RunnerException;
import ru.mifi.service.risk.utils.type.ParamsTypeEnum;
import ru.mifi.utils.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
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
    @Benchmark
    public void calculationParsingTest() {
        String calculationForTest = "(SB_1600_c-SB_1500_c-SB_1400_c+SB_1530_c-SB_1600_p+SB_1500_p+SB_1400_p-SB_1530_p)/(SB_1600_p-SB_1500_p-SB_1400_p+SB_1530_p)*100";
        List<String> result = ParamsTypeEnum.SB.parseCalculationToParams(calculationForTest);
        List<String> correctResult = Arrays.stream("SB_1600_c;SB_1500_c;SB_1400_c;SB_1530_c;SB_1600_p;SB_1500_p;SB_1400_p;SB_1530_p".toUpperCase().split(";")).collect(Collectors.toList());
        Assert.assertEquals(result, correctResult);

    }

    @Test
    public void tempTableNameTest() {
        String timestamp = LocalDateTime.now().toString().replaceAll("[\\-\\:\\.]", "");
        System.out.println("Получился таймстемп: " + timestamp);
    }
    @Test
    public void runBenchMarks() throws IOException, RunnerException {
        String[] ar = new String[0];
        org.openjdk.jmh.Main.main(ar);

    }
}
