package ru.mifi.constructor.treecontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mifi.constructor.service.FormulaService;
import ru.mifi.service.risk.domain.Formula;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/constructor/formulas")
public class FormulaController {


    @Autowired
    FormulaService formulaService;

    @PostMapping(consumes = "application/json")
    public void createFormula(@Valid @RequestBody Formula formula) {
//        TODO responseEntity
        formulaService.createFormula(formula);
    }

    @PutMapping(consumes = "application/json")
    public void updateFormula(@Valid @RequestBody Formula formula) {
//        TODO responseEntity
        formulaService.updateFormula(formula);
    }

    @DeleteMapping("/{id}")
    public void deleteFormula(@PathVariable("id") String id) {
//        TODO responseEntity
        formulaService.deleteFormula(id);
    }
}
