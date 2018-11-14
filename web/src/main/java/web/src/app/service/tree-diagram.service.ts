import {Injectable} from '@angular/core';
import {Http} from '@angular/http';
import {RiskModelModel} from '../model/risk-model.model';
import {
  basePath,
  companiesPath,
  constructorPath,
  formulaPath, modelCalcPath,
  modelPath,
  reqOptions,
  reqOptionsJson, rulePath
} from '../shared/config';
import {TreeNode} from "primeng/api";
import {Router} from "@angular/router";
import {BehaviorSubject, Observable} from "rxjs";

@Injectable()
export class TreeDiagramService {

  constructor(private http: Http, private router: Router) {
    this.modelsLoaded = new BehaviorSubject<boolean>(false);
    this.nodesLoaded = new BehaviorSubject<boolean>(false);
  }

  models: any;
  modelsNodes;
  nodesLoaded: BehaviorSubject<boolean>;
  modelsLoaded: BehaviorSubject<boolean>;

  saveFormula(formula) {
    console.log('service save formula ', formula);
    if (formula.id) {
      return this.updateFormula(formula);
    } else {
      return this.createFormula(formula);
    }
  }

  deleteFormula(formula) {
    console.log('delete fromula method', formula);
    return this.http.delete(basePath + constructorPath + formulaPath + '/' + formula.id, reqOptions);

  }

  private updateFormula(formula) {
    return this.http.put(basePath + constructorPath + formulaPath, JSON.stringify(formula), reqOptionsJson);
  }

  private createFormula(formula) {
    return this.http.post(basePath + constructorPath + formulaPath, JSON.stringify(formula), reqOptionsJson);
  }

  saveRule(rule) {
    console.log('service save rule ', rule);
    if (rule.id) {
      return this.updateRule(rule);
    } else {
      return this.createRule(rule);
    }
  }

  deleteRule(rule) {
    console.log('delete rule method', rule);
    return this.http.delete(basePath + constructorPath + rulePath + '/' + rule.id, reqOptions);

  }

  private updateRule(rule) {
    return this.http.put(basePath + constructorPath + rulePath, JSON.stringify(rule), reqOptionsJson);
  }

  private createRule(rule) {
    return this.http.post(basePath + constructorPath + rulePath, JSON.stringify(rule), reqOptionsJson);
  }

  saveModel(model) {
    console.log('service save model ', model);
    if (model.id) {
      return this.updateModel(model);
    } else {
      return this.createModel(model);
    }
  }

  deleteModel(model) {
    console.log('delete fromula method', model);
    return this.http.delete(basePath + constructorPath + modelPath + '/' + model.id, reqOptions);

  }

  private updateModel(model) {
    return this.http.put(basePath + constructorPath + modelPath, JSON.stringify(model), reqOptionsJson);
  }

  private createModel(model) {
    return this.http.post(basePath + constructorPath + modelPath, JSON.stringify(model), reqOptionsJson);
  }

  saveModelCalc(modelCalc) {
    if (modelCalc.node) {
      return this.updateModelCalc(modelCalc);
    } else {
      return this.createModelCalc(modelCalc);
    }
  }

  deleteModelCalc(modelCalc) {
    console.log('delete fromula method', modelCalc);
    return this.http.delete(basePath + constructorPath + modelCalcPath + '/' + modelCalc.id, reqOptions);

  }

  private updateModelCalc(modelCalc) {
    return this.http.put(basePath + constructorPath + modelCalcPath, JSON.stringify(modelCalc), reqOptionsJson);
  }

  private createModelCalc(modelCalc) {
    return this.http.post(basePath + constructorPath + modelCalcPath, JSON.stringify(modelCalc), reqOptionsJson);
  }

   getAllModels() {
    //TODO MODELS only
    return this.http.get(basePath + constructorPath + modelPath, reqOptions);
  }

  private getAllNodes() {

    return this.http.get(basePath + constructorPath + modelPath + "/toNodes", reqOptions);
  }

  getTreeNodeDTOs() {
    this.modelsNodes = [];
    this.setTheBooleanNodes(false);
    this.getAllNodes().toPromise().then(res => {
      for (let node of res.json()) {
        this.modelsNodes.push(node);
      }
      // this.setTheBooleanNodes(true);
    });
    return this.modelsNodes;
  }

  getModelsAndConvert() {
    this.modelsNodes = [];
    this.setTheBoolean(false);
    this.getAllModels().toPromise().then(res => {
      this.modelsToTreeNode(res.json());
      // this.setTheBoolean(true);
    });
    return this.modelsNodes;
  }


  getTheBoolean(): Observable<boolean> {
    return this.modelsLoaded.asObservable();
  }

  getTheBooleanNodes(): Observable<boolean> {
    return this.nodesLoaded.asObservable();
  }

  setTheBoolean(newValue: boolean): void {
    console.log('boolean in tree-diagr service', newValue);
    this.modelsLoaded.next(newValue);
  }

  private setTheBooleanNodes(newValue: boolean) {
    this.nodesLoaded.next(newValue);

  }

  private modelsToTreeNode(models) {
    for (let mod of models) {
      this.modelsNodes.push(this.modelToTreeNode(mod));
    }
  }

  private modelToTreeNode(mod): any {
    let modelCalcsTreeNodes = [];
    if (mod.modelCalcs !== undefined) {
      for (let modelCalc of mod.modelCalcs) {
        let modelCalcToTreeNode = this.modelCalcToTreeNode(modelCalc, mod);
        modelCalcsTreeNodes.push(modelCalcToTreeNode.guid);
        this.modelsNodes.push(modelCalcToTreeNode)
      }
    }
    return {
      guid: (mod.id + '_' + mod.descr).toString(),
      displayName: mod.descr,
      updated: false,
      type: 'model',
      data: {
        id: mod.id,
        name: mod.descr
      },
      children: modelCalcsTreeNodes
    };
  }

  private modelCalcToTreeNode(modelCalc, parent) {
    let formulaTreeNodes = [];
    if (modelCalc.formulas !== undefined) {
      for (let formula of modelCalc.formulas) {
        let formulaToTreeNode = this.formulaToTreeNode(formula, modelCalc);
        formulaTreeNodes.push(formulaToTreeNode.guid);
        this.modelsNodes.push(formulaToTreeNode);
      }
    }
    return {
      guid: (modelCalc.id + '_' + modelCalc.name).toString(),
      parentId: (parent.id + '_' + parent.descr).toString(),
      displayName: modelCalc.descr,
      updated: false,
      type: 'modelCalc',
      data: {
        id: modelCalc.id,
        name: modelCalc.name
      },
      children: formulaTreeNodes
    }
  }

  private formulaToTreeNode(formula, parent) {
    return {
      guid: (formula.id + '_' + formula.descr).toString(),
      parentId: (parent.id + '_' + parent.name).toString(),
      displayName: formula.descr,
      updated: false,
      type: 'formula',
      data: {
        id: formula.id,
        descr: formula.descr,
        calculation: formula.calculation,
        formulaType: formula.formulaType,
        a: formula.a,
        b: formula.b,
        c: formula.c,
        d: formula.d,
        xb: formula._xb,
        comments: formula.comments,
        rule_id: parent.id,
      },
      children: []
    }
  }



}
