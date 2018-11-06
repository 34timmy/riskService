package ru.mifi.constructor.treecontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mifi.constructor.model.ModelCalc;
import ru.mifi.constructor.service.ModelCalcService;

import javax.validation.Valid;

@RestController
@RequestMapping("/constructor/modelCalcs")
public class ModelCalcController {

    @Autowired
    ModelCalcService modelCalcService;

    @PutMapping(consumes = "application/json")
    public void createModelCalc(@Valid @RequestBody ModelCalc modelCalc) {
//        TODO responseEntity
        modelCalcService.createModelCalc(modelCalc);
    }

    @PostMapping(consumes = "application/json")
    public void updateModelCalc(@Valid @RequestBody ModelCalc modelCalc) {
//        TODO responseEntity
        modelCalcService.updateModelCalc(modelCalc);
    }

    @DeleteMapping()
    public void deleteModelCalc(@RequestParam("id") String id) {
//        TODO responseEntity
        modelCalcService.deleteModelCalc(id);
    }
}
