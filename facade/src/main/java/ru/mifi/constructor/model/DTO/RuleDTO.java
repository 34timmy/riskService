package ru.mifi.constructor.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.mifi.constructor.model.Rule;
import ru.mifi.service.risk.domain.Formula;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class RuleDTO {

    private Map<String, String> data = new HashMap<>();
    private List<Formula> children = new ArrayList<>();

    public RuleDTO(Rule rule) {
        data.put(rule.getId(), rule.getName());
        children.addAll(rule.getFormulas());
    }
}
