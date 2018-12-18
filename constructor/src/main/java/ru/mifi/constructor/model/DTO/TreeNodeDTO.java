package ru.mifi.constructor.model.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.mifi.constructor.model.Model;
import ru.mifi.constructor.model.ModelCalc;
import ru.mifi.service.risk.domain.Formula;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Data
public class TreeNodeDTO {

    private String guid;
    private String parentId;
    private String displayName;
    private boolean updating = false;
    private String type;
    private Object data;
    private boolean is_leaf = false;
    private Set<String> children = new HashSet<String>();
    private List<TreeNodeDTO> resultList;


    private TreeNodeDTO(Model model) {
        this.guid = model.getId();
        this.parentId = null;
        this.displayName = model.getDescr();
        this.type = model.getClass().getSimpleName().toLowerCase();
        this.data = model;
        for (ModelCalc modelCalc : model.getModelCalcs()) {
            if (modelCalc.getParent_id() == null) {
                this.children.add(modelCalc.getNode());
            }
        }
    }

    private TreeNodeDTO(List<ModelCalc> modelCalcList, ModelCalc modelCalc, Map<String, Formula> formulas) {
        this.guid = modelCalc.getNode();
        if ((modelCalc.getParent_id()) != null) {
            this.parentId = modelCalc.getParent_id();
        } else {
            this.parentId = modelCalc.getModel_id();
        }
        this.displayName = modelCalc.getDescr();
        if (modelCalc.is_leaf()) {
            Formula formula = formulas.get(this.guid);
            if (formula!=null) {
            this.is_leaf = true;
            this.data = formula;
            this.type = formula.getClass().getSimpleName().toLowerCase();}
        } else {
            this.type = modelCalc.getClass().getSimpleName().toLowerCase();
            this.data = modelCalc;
        }
        for (ModelCalc mc : modelCalcList) {
            if (this.getGuid().equals(mc.getParent_id()))
                this.children.add(mc.getNode());
        }
    }

    public TreeNodeDTO(Formula formula) {
        this.guid = formula.getId();
        this.parentId = formula.getModel_calc_id();
        this.displayName = formula.getDescr();
        this.type = formula.getClass().getSimpleName().toLowerCase();
        this.data = formula;
    }

    public TreeNodeDTO(List<Model> models, List<Formula> formulas) {
        resultList = new ArrayList<>();
        for (Model model : models) {
            if (model==null) continue;
            resultList.add(new TreeNodeDTO(model));
            for (ModelCalc modelCalc : model.getModelCalcs()) {
                if (modelCalc==null) continue;
                resultList.add(new TreeNodeDTO(model.getModelCalcs(), modelCalc,
                        formulas.stream().collect(Collectors.toMap(Formula::getId, Function.identity()))));
            }
        }
    }
}
