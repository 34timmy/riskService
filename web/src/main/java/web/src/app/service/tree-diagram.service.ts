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
    return this.http.post(basePath + constructorPath + formulaPath, JSON.stringify(formula), reqOptionsJson);
  }

  private createFormula(formula) {
    return this.http.put(basePath + constructorPath + formulaPath, JSON.stringify(formula), reqOptionsJson);
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
    return this.http.post(basePath + constructorPath + rulePath, JSON.stringify(rule), reqOptionsJson);
  }

  private createRule(rule) {
    return this.http.put(basePath + constructorPath + rulePath, JSON.stringify(rule), reqOptionsJson);
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
    return this.http.post(basePath + constructorPath + modelPath, JSON.stringify(model), reqOptionsJson);
  }

  private createModel(model) {
    return this.http.put(basePath + constructorPath + modelPath, JSON.stringify(model), reqOptionsJson);
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
    return this.http.post(basePath + constructorPath + modelCalcPath, JSON.stringify(modelCalc), reqOptionsJson);
  }

  private createModelCalc(modelCalc) {
    return this.http.put(basePath + constructorPath + modelCalcPath, JSON.stringify(modelCalc), reqOptionsJson);
  }

  private getAll() {
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
    this.getAll().toPromise().then(res => {
      this.modelsToTreeNode(res.json());
      // this.setTheBoolean(true);
    });
    return this.modelsNodes;
  }

  HARDCODEDgetModelsAndConvert() {
    this.modelsNodes = [
      {
        "guid": "bc4c7a02-5379-4046-92be-12c67af4295a",
        "displayName": "Elentrix",
        updated: false,
        "children": [
          "85d412c2-ebc1-4d56-96c9-7da433ac9bb2",
          "28aac445-83b1-464d-9695-a4157dab6eac"
        ]
      },
      {
        "guid": "097b8d7c-e0d3-483d-9770-cb5306f7801c",
        "displayName": "Insuron",
        updated: false,
        "children": [
          "85d412c2-ebc1-4d56-96c9-7da433ac9bb2",
          "a2d8ec53-de45-4182-af74-58c27dc8c06c",
          "6ceb08e1-3da5-4532-a5d8-437fe714b685"
        ]
      },
      {
        "guid": "a2d8ec53-de45-4182-af74-58c27dc8c06c",
        "displayName": "Plasmox",
        "parentId": "097b8d7c-e0d3-483d-9770-cb5306f7801c",
        updated: false,
        "children": [
          "c46390bf-31be-4cb6-b91c-15cd55031d32",
          "96ce37a7-3e3d-40b2-96e9-e887ff75a89b",
          "cd4498c4-0ea0-488b-8f58-135bd29e10fc"
        ]
      },
      {
        "guid": "6ceb08e1-3da5-4532-a5d8-437fe714b685",
        "displayName": "Earthwax",
        "parentId": "097b8d7c-e0d3-483d-9770-cb5306f7801c",
        updated: false,
        "children": [
          "e36b619a-bfa0-4db8-aac2-e28f660324ad",
          "7ebeb305-d581-4500-85b6-e28a46610727",
          "d1a75547-d6eb-474c-ab71-b71f797b7010"
        ]
      },
      {
        "guid": "c46390bf-31be-4cb6-b91c-15cd55031d32",
        "displayName": "Savvy",
        "parentId": "a2d8ec53-de45-4182-af74-58c27dc8c06c",
        updated: false,
        "children": [
          "95bc2be9-2f20-411e-a13f-0f03d0ff1aa4",
          "b6938a3a-8405-4b55-bd8a-d7f088c0b5a3"
        ]
      },
      {
        "guid": "96ce37a7-3e3d-40b2-96e9-e887ff75a89b",
        "displayName": "Zizzle",
        "parentId": "a2d8ec53-de45-4182-af74-58c27dc8c06c",
        updated: false,
        "children": [
          "8b82a0b6-56bb-47fe-90e5-e1a107e90208",
          "0ec66087-442f-4663-84f6-f6f99cde0595",
          "7d3abeb6-f864-4b57-bc1a-ef1c4114a571"
        ]
      },
      {
        "guid": "cd4498c4-0ea0-488b-8f58-135bd29e10fc",
        "displayName": "Cubicide",
        "parentId": "a2d8ec53-de45-4182-af74-58c27dc8c06c",
        updated: false,
        "children": [
          "bbf7794c-b6ad-4f2c-9a3a-28c3287bf049"
        ]
      },
      {
        "guid": "28aac445-83b1-464d-9695-a4157dab6eac",
        "displayName": "Cytrek",
        updated: false,
        "parentId": "bc4c7a02-5379-4046-92be-12c67af4295a",
        "children": []
      },
      {
        "guid": "e36b619a-bfa0-4db8-aac2-e28f660324ad",
        "displayName": "Inventure",
        updated: false,
        "parentId": "6ceb08e1-3da5-4532-a5d8-437fe714b685",
        "children": [
          "67e4fd2b-bdaf-47aa-bb2e-ed89a7a87db2"
        ]
      },
      {
        "guid": "7ebeb305-d581-4500-85b6-e28a46610727",
        "displayName": "Pyramia",
        updated: false,
        "parentId": "6ceb08e1-3da5-4532-a5d8-437fe714b685",
        "children": [
          "e848a18c-b9ba-4cd1-a749-af89b2442666"
        ]
      },
      {
        "guid": "d1a75547-d6eb-474c-ab71-b71f797b7010",
        "displayName": "Apexia",
        updated: false,
        "parentId": "6ceb08e1-3da5-4532-a5d8-437fe714b685",
        "children": []
      },
      {
        "guid": "95bc2be9-2f20-411e-a13f-0f03d0ff1aa4",
        "displayName": "Futurity",
        updated: false,
        "parentId": "c46390bf-31be-4cb6-b91c-15cd55031d32",
        "children": []
      },
      {
        "guid": "b6938a3a-8405-4b55-bd8a-d7f088c0b5a3",
        "displayName": "Cytrak",
        updated: false,
        "parentId": "c46390bf-31be-4cb6-b91c-15cd55031d32",
        "children": []
      },
      {
        "guid": "8b82a0b6-56bb-47fe-90e5-e1a107e90208",
        "displayName": "Zentury",
        updated: false,
        "parentId": "96ce37a7-3e3d-40b2-96e9-e887ff75a89b",
        "children": []
      },
      {
        "guid": "0ec66087-442f-4663-84f6-f6f99cde0595",
        "displayName": "Unia",
        updated: false,
        "parentId": "96ce37a7-3e3d-40b2-96e9-e887ff75a89b",
        "children": []
      },
      {
        "guid": "7d3abeb6-f864-4b57-bc1a-ef1c4114a571",
        "displayName": "Brainquil",
        updated: false,
        "parentId": "96ce37a7-3e3d-40b2-96e9-e887ff75a89b",
        "children": []
      },
      {
        "guid": "bbf7794c-b6ad-4f2c-9a3a-28c3287bf049",
        "displayName": "Valpreal",
        updated: false,
        "parentId": "cd4498c4-0ea0-488b-8f58-135bd29e10fc",
        "children": []
      },
      {
        "guid": "67e4fd2b-bdaf-47aa-bb2e-ed89a7a87db2",
        "displayName": "Tubesys",
        updated: false,
        "parentId": "e36b619a-bfa0-4db8-aac2-e28f660324ad",
        "children": []
      },
      {
        "guid": "e848a18c-b9ba-4cd1-a749-af89b2442666",
        "displayName": "Kage",
        updated: false,
        "parentId": "7ebeb305-d581-4500-85b6-e28a46610727",
        "children": []
      },
      {
        "guid": "85d412c2-ebc1-4d56-96c9-7da433ac9bb2",
        "displayName": "Asimiline",
        updated: false,
        "parentId": "bc4c7a02-5379-4046-92be-12c67af4295a",
        "children": []
      }
    ];
    this.setTheBoolean(false);
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
