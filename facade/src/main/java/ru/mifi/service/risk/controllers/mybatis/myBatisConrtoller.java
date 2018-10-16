package ru.mifi.service.risk.controllers.mybatis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mifi.service.risk.database.myBatis.CalculationAccessorMapper;
import ru.mifi.service.risk.domain.CompanyParam;
import ru.mifi.service.risk.domain.FormulaParam;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/mybatis" +
        "/getData")
public class myBatisConrtoller {

    @Autowired
    CalculationAccessorMapper mapper;

    @GetMapping("/formulaParams")
    List<CompanyParam> getFormulaParams() {
        Set<String> companyIds = new HashSet<>();
        companyIds.add("1");
        return mapper.getFormulaParamsScript("1", companyIds);
    }
}
