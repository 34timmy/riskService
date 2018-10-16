package ru.mifi.constructor.treecontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mifi.constructor.model.Rule;
import ru.mifi.constructor.service.RuleService;

import javax.validation.Valid;

@RestController
@RequestMapping("/rule")
public class RuleController {

    @Autowired
    RuleService ruleService;

    @PutMapping(consumes = "application/json")
    public void createRule(@Valid @RequestBody Rule rule) {
//        TODO responseEntity
        ruleService.createRule(rule);
    }

    @PostMapping(consumes = "application/json")
    public void updateRule(@Valid @RequestBody Rule rule) {
//        TODO responseEntity
        ruleService.updateRule(rule);
    }

    @DeleteMapping()
    public void deleteRule(@RequestParam("id") String id) {
//        TODO responseEntity
        ruleService.deleteRule(id);
    }
}
