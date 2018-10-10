package ru.mifi.constructor.model.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.mifi.constructor.model.Model;
import ru.mifi.constructor.model.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class ModelDTO implements Node {
    private Map<String, String> data = new HashMap<>();
    private List<RuleDTO> children = new ArrayList<>();

    public ModelDTO(Model model) {
        this.data.put(model.getId(), model.getName());
        for (Rule rule : model.getRules()) {
            children.add(new RuleDTO(rule));
        }
    }
}
