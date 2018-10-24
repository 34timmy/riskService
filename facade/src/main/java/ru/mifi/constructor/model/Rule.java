package ru.mifi.constructor.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.mifi.service.risk.domain.Formula;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Rule {
    private String id;
    private String name;
    private List<Formula> formulas;
    private String model_id;
}
