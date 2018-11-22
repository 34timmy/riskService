import {Injectable} from '@angular/core';
import {Headers, Http, RequestOptions} from '@angular/http';
import {basePath, constructorPath, formulaPath, modelPath} from '../shared/config';
import {TreeNode} from "primeng/api";
import {Router} from "@angular/router";
import {BehaviorSubject, Observable} from "rxjs";
import {v4 as uuid} from 'uuid';
import {AuthService} from "./auth.service";

@Injectable()
export class TreeService {
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
  modelsNodes: TreeNode[] = [];
  modelsLoaded: BehaviorSubject<boolean>;

  saveFormula(formula) {
    console.log('service save formula ', formula);
    if (formula.id) {
      return this.update(formula);
    } else {
      formula.id = uuid();
      return this.create(formula);
    }
  }

  delete(formula) {
    return this.http.delete(basePath + constructorPath + formulaPath + '/' + formula.id, this.reqOptions);

  }

  private update(formula) {
    return this.http.post(basePath + constructorPath + formulaPath, JSON.stringify(formula), this.reqOptionsJson);
  }

  private create(formula) {
    return this.http.put(basePath + constructorPath + formulaPath, JSON.stringify(formula), this.reqOptionsJson);
  }

  private getAll() {
    return this.http.get(basePath + constructorPath + modelPath, this.reqOptions);
  }

  getListOfResults() {
    return this.http.get(basePath +
      "/getData" +
      "/getListOfResults"
      , this.reqOptions)
  }


  getResultTableNamesRequest(modelId, companyListId, allCompaniesListId, year) {
    return this.http.get(basePath +
      "/getData" +
      "/allTablesByParams?" +
      "modelId=" + modelId +
      "&companyListId=" + companyListId +
      "&allCompaniesListId=" + allCompaniesListId +
      "&year=" + year
      , this.reqOptions);
  }

  //  getResults(tableName) {
  //   // this.modelsNodes = [];
  //   // this.setTheBoolean(false);
  //   return this.getCalcResultDTOs(tableName)
  //   //   .toPromise().then(res => {
  //   //   this.modelsToTreeNode(res.json());
  //   //   this.setTheBoolean(true);
  //   // });
  //   // return this.modelsNodes;
  // }

  getCalcResultDTOs(tableName) {
    return this.http.get(basePath +
      "/getData" +
      "/byTable?" +
      "tableName=" + tableName
      , this.reqOptions);
  }

    getTheBoolean(): Observable<boolean> {
    return this.modelsLoaded.asObservable();
  }

  private setTheBoolean(newValue: boolean): void {
    this.modelsLoaded.next(newValue);
  }

  // private modelsToTreeNode(models) {
  //   for (let mod of models) {
  //     this.modelsNodes.push(this.modelToTreeNode(mod));
  //   }
  // }
  //
  // private modelToTreeNode(mod): TreeNode {
  //   let rulesTreeNodes: TreeNode[] = [];
  //   if (mod.rules !== undefined) {
  //     for (let rule of mod.rules) {
  //       rulesTreeNodes.push(this.ruleToTreeNode(rule, mod))
  //     }
  //   }
  //   return {
  //     label: mod.nom,
  //     data: mod,
  //     children: rulesTreeNodes,
  //     draggable: true,
  //     droppable: true
  //   };
  // }
  //
  // private ruleToTreeNode(rule, parent) {
  //   let formulaTreeNodes: TreeNode[] = [];
  //   if (rule.formulas !== undefined) {
  //     for (let formula of rule.formulas) {
  //       formulaTreeNodes.push(this.formulaToTreeNode(formula, rule))
  //     }
  //   }
  //   return {
  //     label: rule.nom,
  //     parent: parent,
  //     data: rule,
  //     children: formulaTreeNodes,
  //     draggable: true,
  //     droppable: true
  //   }
  // }
  //
  // private formulaToTreeNode(formula, parent): TreeNode {
  //   return {
  //     label: formula.nom,
  //     parent: parent,
  //     data: {
  //       id: formula.node,
  //       name: formula.name,
  //       calculation: formula.calculation,
  //       formulaType: formula.formulaType,
  //       a: formula.a,
  //       b: formula.b,
  //       c: formula.c,
  //       d: formula.d,
  //       xb: formula.xb,
  //       comments: formula.comments,
  //       rule_id: parent.id,
  //     },
  //     children: [],
  //     draggable: true,
  //     droppable: true
  //   }
  // }


}
