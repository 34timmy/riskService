import {Component, OnInit, ViewChild} from '@angular/core';

import {NodesListService} from './services/nodesList.service'
import {DomSanitizer} from "@angular/platform-browser";
import {TreeDiagramService} from "../../service/tree-diagram.service";
import {BehaviorSubject, Observable, of} from "rxjs";
import {FormulaEditComponent} from "./formula/formula-edit.component";
import {ConfirmationService, MessageService} from "primeng/api";
import {ModelEditComponent} from "./model/model-edit.component";
import {ModelcalcEditComponent} from "./modelcalc/modelcalc-edit.component";
import {ChooseComponent} from "./choose_dialog/choose.component";

@Component({
  selector: 'tree-diagram',
  styleUrls: ['./tree.component.scss'],
  templateUrl: './tree.component.html',
})
export class Tree implements OnInit {

  @ViewChild('formulaChild')
  private formulaEditChild: FormulaEditComponent;

  @ViewChild('ruleChild')
  private ruleEditChild: FormulaEditComponent;

  @ViewChild('modelChild')
  private modelEditChild: ModelEditComponent;

  @ViewChild('modelCalcChild')
  private modelCalcEditChild: ModelcalcEditComponent;

  @ViewChild('chooseChild')
  private chooseChild: ChooseComponent;


  private _config = {
    confirmationService: this.confirmationService,
    notificationService: this.notificationService,
    treeService: this.treeService,
    formulaEditChild: this.formulaEditChild,
    ruleEditChild: this.ruleEditChild,
    modelEditChild: this.modelEditChild,
    modelCalcEditChild: this.modelCalcEditChild,
    chooseChild: this.chooseChild,
    nodeWidth: 200,
    nodeHeight: 100
  };

  private paneDragging = false
  private paneTransform
  private zoom = 1
  private paneX = 0
  private paneY = 0
  nodes;
  updatedNodes;
  data;
  typeDialog;
  modelsLoaded: Observable<boolean>;
  reloadModels = new BehaviorSubject<boolean>(false);
  showToggle = false;

  constructor(
    private nodesSrv: NodesListService,
    private sanitizer: DomSanitizer,
    private treeService: TreeDiagramService,
    private confirmationService: ConfirmationService,
    private notificationService: MessageService
  ) {
    this._config.treeService = this.treeService;
    this._config.confirmationService = this.confirmationService;
    this._config.notificationService = this.notificationService;
  }


  ngOnInit() {
    this.treeService.getTheBoolean().subscribe(value => {
        this.modelsLoaded = of(value);
        console.log('moldesLoaded', this.modelsLoaded)
      }
    );
    this.treeService.getTypeDialog().subscribe(value => {
        this.typeDialog = of(value);
      }
    );

    // if (!_data || !Array.isArray(_data.json)) return
    // if (typeof _data.config === 'object') {
    //   this._config = Object.assign(this._config, _data.config)

    // }

  }

  loadView(modelId) {
    this.treeService.setTheBoolean(false);
    if (modelId != null) {
      this.treeService.getTreeNodeForModelObserv(modelId).subscribe(res => {
        this.data = res.json();
        this.initData();
      });
    }
    else {
      this.data = [];
      setTimeout(() => this.initData(), 0);
    }
  }

  initData() {
    this.reloadModels.next(false);
    this._config.formulaEditChild = this.formulaEditChild;
    this._config.ruleEditChild = this.ruleEditChild;
    this._config.modelEditChild = this.modelEditChild;
    this._config.modelCalcEditChild = this.modelCalcEditChild;
    this._config.chooseChild = this.chooseChild;
    this.chooseChild.setModelCalcChild(this.modelCalcEditChild);
    this.chooseChild.setFormulaChild(this.formulaEditChild);
    this.nodes = this.nodesSrv.loadNodes(this.data, this._config);
    this.treeService.setTheBoolean(true)

  }

  public newNode() {
    this.nodesSrv.newNode()
  }

  public get nodeMaker() {
    return this.nodesSrv.makerNode()
  }

  public onmousedown(event) {
    this.paneDragging = true;
  }

  public onmousemove(event) {
    if (this.paneDragging) {
      let {movementX, movementY} = event
      this.paneX += movementX
      this.paneY += movementY
      this.makeTransform()
    }
  }

  public onmouseup() {
    this.paneDragging = false
  }

  public makeTransform() {
    this.paneTransform = this.sanitizer.bypassSecurityTrustStyle(`translate(${this.paneX }px, ${this.paneY}px) scale(${this.zoom})`)
  }

  public preventMouse(event) {
    event.stopPropagation()
  }

  public onmousewheel(event) {
    let delta;
    event.preventDefault();
    delta = event.detail || event.wheelDelta;
    this.zoom += delta / 1000 / 2;
    this.zoom = Math.min(Math.max(this.zoom, 0.2), 3);
    this.makeTransform()
  }

  private onChooseAgr() {
    this.chooseChild.chooseAgr();
  }

  private onChooseFormula() {
    this.chooseChild.chooseFormula();
  }

  private onSaveFormula(node) {
    console.log('formula ', node);
    this.treeService.saveFormula(node)
      .subscribe(
        res => {
          if (!node.creating) {
            this.nodesSrv.editNodeOnSaveFormula();
            this.successMessage(node,
              'Запись ' + node.descr + ' изменена',
              null);
          }
          else {

            let modelCalc = {
              node: node.guid,
              descr: node.descr,
              model_id: node.model_id,
              parent_node: node.node,
              weight: node.weight,
              is_leaf: node.is_leaf,
              level: 0,
              creating: true,
              model_calc_id: node.model_calc_id,
            };
            this.treeService.saveModelCalc(modelCalc).subscribe(res => {
            }, err => {
              this.errorMessage(err.json());
            });

            this.nodesSrv.addNodeOnSaveFormula();
            this.successMessage(node,
              'Запись ' + node.descr + ' добавлена',
              null);
          }
        },
        err => {
          this.errorMessage(err.json());
        }
      );
  }

  private onDeleteFormula(formula) {
    this.treeService.deleteFormula(formula).subscribe(
      res => {
      }
    );
  }

  private onSaveRule(node) {
    console.log('rule ', node);
    this.treeService.saveRule(node)
      .subscribe(
        res => {
          if (!node.creating) {
            this.nodesSrv.editNodeOnSaveRule();
            this.successMessage(node,
              'Запись ' + node.descr + ' изменена',
              null);
          }
          else {
            this.nodesSrv.addNodeOnSaveRule();
            this.successMessage(node,
              'Запись ' + node.descr + ' добавлена',
              null);

          }

        },
        err => {
          this.errorMessage(err.json());
        }
      );
  }

  private onSaveModel(node) {
    console.log('model ', node);
    this.treeService.saveModel(node)
      .subscribe(
        res => {
          if (!node.creating) {
            this.nodesSrv.editNodeOnSaveModel();
            this.successMessage(node,
              'Запись ' + node.descr + ' изменена',
              null);
          }
          else {
            this.nodesSrv.addNodeOnSaveModel();
            this.reloadModels.next(true);
            this.successMessage(node,
              'Запись ' + node.descr + ' добавлена',
              null);

          }
        },
        err => {
          this.errorMessage(err.json());
        }
      );
  }

  private onSaveModelCalc(node) {
    console.log('model ', node);
    this.treeService.saveModelCalc(node)
      .subscribe(
        res => {
          if (!node.creating) {
            this.nodesSrv.editNodeOnSaveModelCalc();
            this.successMessage(node,
              'Запись ' + node.descr + ' изменена',
              null);

          }
          else {
            this.nodesSrv.addNodeOnSaveModelCalc();
            this.successMessage(node,
              'Запись ' + node.descr + ' добавлена',
              null);

          }
        },
        err => {
          this.errorMessage(err.json());
        }
      );
  }

  private saveChanges(nodes) {
    //TODO save changes after drag and drop
    this.updatedNodes = Array.from(this.nodesSrv.getNodes())
      .filter((val) => val.updated === true);
    console.log('updatedNodes ', this.updatedNodes)
  }

  private successMessage(obj, summary, detail) {
    if (detail == null) {
      this.notificationService.add({
        severity: 'success',
        summary: summary,
        detail: JSON.stringify(obj, null, 2)
      })
    } else {
      this.notificationService.add({
        severity: 'success',
        summary: summary,
        detail: detail
      })
    }
  }

  private errorMessage(error) {
    this.notificationService.add({
      severity: 'error',
      summary: error.cause,
      detail: error.url + '\n' + error.detail
    })
  }


  show(modelid) {
    this.loadView(modelid);
    this.showToggle = true;
  }

  closeModal() {
    this.showToggle = false;
  }

}
