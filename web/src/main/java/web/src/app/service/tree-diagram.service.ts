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
import {Router} from "@angular/router";
import {BehaviorSubject, Observable} from "rxjs";

@Injectable()
export class TreeDiagramService {

  constructor(private http: Http, private router: Router) {
    this.modelsLoaded = new BehaviorSubject<boolean>(false);
  }

  models: any;
  modelsNodes;
  modelsLoaded: BehaviorSubject<boolean>;


  private getAll() {
    return this.http.get(basePath + constructorPath + modelPath, reqOptions);
  }

  getModelsAndConvert() {
    this.modelsNodes = [];
    this.setTheBoolean(false);
    this.getAll().toPromise().then(res => {
      this.modelsToTreeNode(res.json());
      this.setTheBoolean(true);
    });
    return this.modelsNodes;
  }

  getTheBoolean(): Observable<boolean> {
    return this.modelsLoaded.asObservable();
  }

  private setTheBoolean(newValue: boolean): void {
    this.modelsLoaded.next(newValue);
  }

  private modelsToTreeNode(models) {
    for (let mod of models) {
      this.modelsNodes.push(this.modelToTreeNode(mod));
    }
  }

  private modelToTreeNode(mod): any {
    let rulesTreeNodes = [];
    if (mod.rules !== undefined) {
      for (let rule of mod.rules) {
        let ruleToTreeNode = this.ruleToTreeNode(rule, mod);
        rulesTreeNodes.push(ruleToTreeNode.guid);
        this.modelsNodes.push(ruleToTreeNode)
      }
    }
    return {
      guid: (mod.id + '_' + mod.name).toString(),
      displayName: mod.name,
      data: {
        id: mod.id,
        name: mod.name
      },
      children: rulesTreeNodes
    };
  }

  private ruleToTreeNode(rule, parent) {
    let formulaTreeNodes = [];
    if (rule.formulas !== undefined) {
      for (let formula of rule.formulas) {
        let formulaToTreeNode = this.formulaToTreeNode(formula, rule);
        formulaTreeNodes.push(formulaToTreeNode.guid);
        this.modelsNodes.push(formulaToTreeNode);
      }
    }
    return {
      guid: (rule.id + '_' + rule.name).toString(),
      parentId: (parent.id + '_' + parent.name).toString(),
      displayName: rule.name,
      data: {
        id: rule.id,
        name: rule.name
      },
      children: formulaTreeNodes
    }
  }

  private formulaToTreeNode(formula, parent) {
    return {
      guid: (formula.id + '_' + formula.name).toString(),
      parentId: (parent.id + '_' + parent.name).toString(),
      displayName: formula.name,
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
