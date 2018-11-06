package ru.mifi.constructor.model.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.mifi.constructor.model.Model;
import ru.mifi.constructor.model.ModelCalc;
import ru.mifi.service.risk.domain.Formula;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@RequiredArgsConstructor
public class TreeNodeDTO {

    private String guid;
    private String parentId;
    private String displayName;
    private boolean updated = false;
    private String type;
    private Object data;
    private Set<String> children = new HashSet<String>();
    private List<TreeNodeDTO> resultList;


    public TreeNodeDTO(Model model) {
        this.guid = model.getId();
        this.parentId = null;
        this.displayName = model.getDescr();
        this.type = model.getClass().getSimpleName().toLowerCase();
        this.data = model;
        for (ModelCalc modelCalc : model.getModelCalcs()) {
            if (modelCalc.getParent_node() == null) {
                this.children.add(modelCalc.getNode());
            }
        }
    }

    public TreeNodeDTO(List<ModelCalc> modelCalcList, ModelCalc modelCalc) {
        this.guid = modelCalc.getNode();
        if ((modelCalc.getParent_node()) != null) {
            this.parentId = modelCalc.getParent_node();
        } else {
            this.parentId = modelCalc.getModel_id();
        }
        this.displayName = modelCalc.getDescr();
        this.type = modelCalc.getClass().getSimpleName().toLowerCase();
        this.data = modelCalc;
        for (ModelCalc mc : modelCalcList) {
            if (this.getGuid().equals(mc.getParent_node()))
                this.children.add(mc.getNode());
        }
        for (Formula formula : modelCalc.getFormulas()) {
            this.children.add(formula.getId());
        }
    }

    public TreeNodeDTO(Formula formula) {
        this.guid = formula.getId();
        this.parentId = formula.getModel_calc_id();
        this.displayName = formula.getDescr();
        this.type = formula.getClass().getSimpleName().toLowerCase();
        this.data = formula;
    }

    public TreeNodeDTO(List<Model> models) {
        resultList = new ArrayList<TreeNodeDTO>();
        for (Model model : models) {
            resultList.add(new TreeNodeDTO(model));
            for (ModelCalc modelCalc : model.getModelCalcs()) {
                resultList.add(new TreeNodeDTO(model.getModelCalcs(), modelCalc));
                for (Formula formula : modelCalc.getFormulas()) {
                    resultList.add(new TreeNodeDTO(formula));
                }
            }
        }
    }
}
