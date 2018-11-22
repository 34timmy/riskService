import {Injectable} from '@angular/core';
import {Headers, Http, RequestOptions} from '@angular/http';
import {
  basePath,
  constructorPath,
  formulaPath,
  modelCalcPath,
  modelPath,
  rulePath
} from '../shared/config';
import {Router} from "@angular/router";
import {BehaviorSubject, Observable} from "rxjs";
import {AuthService} from "./auth.service";

@Injectable()
export class TreeDiagramService {
  private headersJson = new Headers({
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + this.authenticationService.getToken()
  });

  private headers = new Headers({
    'Authorization': 'Bearer ' + this.authenticationService.getToken()
  });

  reqOptions: RequestOptions = new RequestOptions({
    withCredentials: true,
    headers: this.headers
  });

  reqOptionsJson: RequestOptions = new RequestOptions({
    withCredentials: true,
    headers: this.headersJson
  });

  constructor(private http: Http, private router: Router,
              private authenticationService: AuthService
  ) {
    this.modelsLoaded = new BehaviorSubject<boolean>(false);
  }

  models: any;
  modelsNodes;
  modelsLoaded: BehaviorSubject<boolean>;
  typeDialog = new BehaviorSubject<boolean>(false);

  saveFormula(formula) {
    console.log('service save formula ', formula);
    if (!formula.creating) {
      return this.updateFormula(formula);
    } else {
      return this.createFormula(formula);
    }
  }

  deleteFormula(formula) {
    console.log('delete fromula method', formula);
    return this.http.delete(basePath + constructorPath + formulaPath + '/' + formula.id, this.reqOptions);

  }

  private updateFormula(formula) {
    return this.http.put(basePath + constructorPath + formulaPath, JSON.stringify(formula), this.reqOptionsJson);
  }

  private createFormula(formula) {
    return this.http.post(basePath + constructorPath + formulaPath, JSON.stringify(formula), this.reqOptionsJson);
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
    return this.http.delete(basePath + constructorPath + rulePath + '/' + rule.id, this.reqOptions);

  }

  private updateRule(rule) {
    return this.http.put(basePath + constructorPath + rulePath, JSON.stringify(rule), this.reqOptionsJson);
  }

  private createRule(rule) {
    return this.http.post(basePath + constructorPath + rulePath, JSON.stringify(rule), this.reqOptionsJson);
  }

  saveModel(model) {
    console.log('service save model ', model);
    if (!model.creating) {
      return this.updateModel(model);
    } else {
      return this.createModel(model);
    }
  }

  deleteModel(model) {
    console.log('delete fromula method', model);
    return this.http.delete(basePath + constructorPath + modelPath + '/' + model.id, this.reqOptions);

  }

  private updateModel(model) {
    return this.http.put(basePath + constructorPath + modelPath, JSON.stringify(model), this.reqOptionsJson);
  }

  private createModel(model) {
    return this.http.post(basePath + constructorPath + modelPath, JSON.stringify(model), this.reqOptionsJson);
  }

  saveModelCalc(modelCalc) {
    if (!modelCalc.creating) {
      return this.updateModelCalc(modelCalc);
    } else {
      return this.createModelCalc(modelCalc);
    }
  }

  deleteModelCalc(modelCalc) {
    console.log('delete fromula method', modelCalc);
    return this.http.delete(basePath + constructorPath + modelCalcPath + '/' + modelCalc.id, this.reqOptions);

  }

  private updateModelCalc(modelCalc) {
    return this.http.put(basePath + constructorPath + modelCalcPath, JSON.stringify(modelCalc), this.reqOptionsJson);
  }

  private createModelCalc(modelCalc) {
    return this.http.post(basePath + constructorPath + modelCalcPath, JSON.stringify(modelCalc), this.reqOptionsJson);
  }

  getAllModelsOnly() {
    return this.http.get(basePath + constructorPath + modelPath, this.reqOptions);
  }

  private getAllNodes() {
    return this.http.get(basePath + constructorPath + modelPath + "/treeNodes", this.reqOptions);
  }

  private getNodeForModel(id) {
    return this.http.get(basePath + constructorPath + modelPath +
      "/treeNodes" +
      "/" + id
      , this.reqOptions);
  }

  getTreeNodeDTOs() {
    this.modelsNodes = [];
    this.getAllNodes().toPromise().then(res => {
      for (let node of res.json()) {
        this.modelsNodes.push(node);
      }
    });
    return this.modelsNodes;
  }

  getTreeNodeForModel(id) {
    this.modelsNodes = [];
    this.getNodeForModel(id).toPromise().then(res => {
      for (let node of res.json()) {
        this.modelsNodes.push(node);
      }
    });
    return this.modelsNodes;
  }

  getTreeNodeForModelObserv(id) {
    return this.getNodeForModel(id);
  }

  getTheBoolean(): Observable<boolean> {
    return this.modelsLoaded.asObservable();
  }


  setTheBoolean(newValue: boolean): void {
    console.log('boolean in tree-diagr service', newValue);
    this.modelsLoaded.next(newValue);
  }


  getTypeDialog() {
    return this.typeDialog.asObservable();
  }

  setTheTypeDalog(newValue: boolean): void {
    this.typeDialog.next(newValue);
  }

  // @Deprecated
  private getModelsAndConvert() {
    this.modelsNodes = [];
    this.setTheBoolean(false);
    // this.getAllModelsOnly().toPromise().then(res => {
    //   this.modelsToTreeNode(res.json());
    //   this.setTheBoolean(true);
    // });
    return this.modelsNodes;
  }

  // @Deprecated
  private modelsToTreeNode(models) {
    for (let mod of models) {
      this.modelsNodes.push(this.modelToTreeNode(mod));
    }
  }

  // @Deprecated
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

  // @Deprecated
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

  // @Deprecated
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
