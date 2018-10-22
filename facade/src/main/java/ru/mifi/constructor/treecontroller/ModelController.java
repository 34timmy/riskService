package ru.mifi.constructor.treecontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.mifi.constructor.model.Model;
import ru.mifi.constructor.service.ModelService;

import javax.validation.Valid;

@RestController
@RequestMapping("/models")
public class ModelController {

    @Autowired
    ModelService modelService;

    @PutMapping(consumes = "application/json")
    public void createModel(@Valid @RequestBody Model model) {
//        TODO responseEntity
        modelService.createModel(model);
    }

    @PostMapping(consumes = "application/json")
    public void updateModel(@Valid @RequestBody Model model) {
//        TODO responseEntity
        modelService.updateModel(model);
    }

    @DeleteMapping()
    public void deleteModel(@RequestParam("id") String id) {
//        TODO responseEntity
        modelService.deleteModel(id);
    }
}
