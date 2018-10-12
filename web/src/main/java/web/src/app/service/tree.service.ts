import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {RiskModelModel} from '../model/risk-model.model';
import {
  basePath,
  companiesPath,
  constructorPath,
  formulaPath,
  modelPath,
  reqOptions,
  reqOptionsJson
} from '../shared/config';
import {TreeNode} from "primeng/api";

@Injectable()
export class TreeService {

  constructor(private http: Http) {
  }

  models: any;
  modelsNodes: TreeNode[] = [];

  saveFormula(formula) {
    console.log('service save formula ', formula);
    if (formula.id) {
      return this.updateFormula(formula);
    } else {
      return this.createFormula(formula);
    }
  }

  private updateFormula(formula) {
    return this.http.post(basePath + constructorPath + formulaPath, JSON.stringify(formula), reqOptionsJson);
  }

  private createFormula(formula) {
    return this.http.put(basePath + constructorPath + formulaPath, JSON.stringify(formula), reqOptionsJson);
  }

  getModels() {
    return this.http.get(basePath + constructorPath + modelPath, reqOptions).toPromise()
      .then(res => <RiskModelModel[]> res.json());
  }

  getModelsAndConvert() {
     this.http.get(basePath + constructorPath + modelPath, reqOptions).toPromise()
      .then(res => {
        this.modelsToTreeNode(res.json());
      });
    return this.modelsNodes;
  }

  modelsToTreeNode(models) {
    for (let mod of models) {
      this.modelsNodes.push(this.modelToTreeNode(mod));
    }
  }

  modelToTreeNode(mod): TreeNode {
    let rulesTreeNodes: TreeNode[] = [];
    if (mod.rules !== undefined) {
      for (let rule of mod.rules) {
        rulesTreeNodes.push(this.ruleToTreeNode(rule, mod))
      }
    }
    return {
      label: mod.nom,
      data: mod,
      children: rulesTreeNodes
    };
  }

  ruleToTreeNode(rule, parent) {
    let formulaTreeNodes: TreeNode[] = [];
    if (rule.formulas !== undefined) {
      for (let formula of rule.formulas) {
        formulaTreeNodes.push(this.formulaToTreeNode(formula, rule))
      }
    }
    return {
      label: rule.nom,
      parent: parent,
      data: rule,
      children: formulaTreeNodes
    }
  }


  formulaToTreeNode(formula, parent): TreeNode {
    return {
      label: formula.nom,
      parent: parent,
      data: {
        id: formula.node,
        name: formula.name,
        calculation: formula.calculation,
        formulaType: formula.formulaType,
        a: formula.a,
        b: formula.b,
        c: formula.c,
        d: formula.d,
        xb: formula.xb,
        comments: formula.comments,
        rule_id: parent.id,
      },
      children: []
    }
  }


}
