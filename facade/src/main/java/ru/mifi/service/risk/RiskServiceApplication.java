package ru.mifi.service.risk;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import ru.mifi.constructor.model.DTO.RuleDTO;

import java.util.Map;

@SpringBootApplication(scanBasePackages = "ru.mifi")
@MapperScan("ru.mifi")
public class RiskServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RiskServiceApplication.class, args);
    }
}
