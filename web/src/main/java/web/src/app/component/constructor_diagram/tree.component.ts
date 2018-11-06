import {Component, OnInit, ViewChild} from '@angular/core';

import {NodesListService} from './services/nodesList.service'
import {DomSanitizer} from "@angular/platform-browser";
import {TreeDiagramService} from "../../service/tree-diagram.service";
import {Observable, of} from "rxjs";
import {FormulaEditComponent} from "../formula/formula-edit.component";
import {ConfirmationService, MessageService} from "primeng/api";
import {ModelEditComponent} from "../model/model-edit.component";
import {ModelcalcEditComponent} from "../modelcalc/modelcalc-edit.component";

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



  private _config = {
    confirmationService: this.confirmationService,
    notificationService: this.notificationService,
    treeService: this.treeService,
    formulaEditChild: this.formulaEditChild,
    ruleEditChild: this.ruleEditChild,
    modelEditChild: this.modelEditChild,
    modelCalcEditChild: this.modelCalcEditChild,
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
  modelsLoaded: Observable<boolean>;
  nodesLoaded: Observable<boolean>;

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
        console.log('is models Loaded? ', this.modelsLoaded)
      }
    );

    let _data = this.treeService.getTreeNodeDTOs();
    // let _data2 = this.treeService.getModelsAndConvert();
    console.log('data', _data)
    // console.log('data2', _data2)
    // let _data = this.treeService.HARDCODEDgetModelsAndConvert();
    // if (!_data || !Array.isArray(_data.json)) return
    // if (typeof _data.config === 'object') {
    //   this._config = Object.assign(this._config, _data.config)
    // }

    // this.treeService.getTheBooleanNodes().subscribe(value => {
    //   if (value) {
    //     setTimeout(() => {
    //         this._config.formulaEditChild = this.formulaEditChild;
    //         this._config.ruleEditChild = this.ruleEditChild;
    //         this._config.modelEditChild = this.modelEditChild;
    //         this.nodes = this.nodesSrv.loadNodes(_data, this._config);
    //         this.treeService.setTheBoolean(true)
    //       }
    //     );
    //
    //   }
    // });
    if (_data) {
      setTimeout(() => {
          this._config.formulaEditChild = this.formulaEditChild;
          this._config.ruleEditChild = this.ruleEditChild;
          this._config.modelEditChild = this.modelEditChild;
          this._config.modelCalcEditChild = this.modelCalcEditChild;
          this.nodes = this.nodesSrv.loadNodes(_data, this._config);
          this.treeService.setTheBoolean(true)
        }
      );
    }
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

  onSaveFormula(node) {
    console.log('formula ', node);
    this.treeService.saveFormula(node)
      .subscribe(
        res => {
          if (node.id) {
            this.nodesSrv.editNodeOnSaveFormula();
            this.successMessage(node, 'изменена');
          }
          else {
            this.nodesSrv.addNodeOnSaveFormula();
            this.successMessage(node, 'добавлена');
          }
        },
        err => {
          this.errorMessage(err.json());
        }
      );
  }

  onDeleteFormula(formula) {
    this.treeService.deleteFormula(formula).subscribe(
      res => {
      }
    );
  }

  onSaveRule(node) {
    console.log('rule ', node);
    this.treeService.saveRule(node)
      .subscribe(
        res => {
          if (node.id) {
            this.nodesSrv.editNodeOnSaveRule();
            this.successMessage(node, 'изменена');
          }
          else {
            this.nodesSrv.addNodeOnSaveRule();
            this.successMessage(node, 'добавлена');
          }

        },
        err => {
          this.errorMessage(err.json());
        }
      );
  }

  onSaveModel(node) {
    console.log('model ', node);
    this.treeService.saveModel(node)
      .subscribe(
        res => {
          if (node.id) {
            this.nodesSrv.editNodeOnSaveModel();
            this.successMessage(node, 'изменена')
          }
          else {
            this.nodesSrv.addNodeOnSaveModel();
            this.successMessage(node, 'добавлена')
          }
        },
        err => {
          this.errorMessage(err.json());
        }
      );
  }

  onSaveModelCalc(node) {
    console.log('model ', node);
    this.treeService.saveModelCalc(node)
      .subscribe(
        res => {
          if (node.node) {
            this.nodesSrv.editNodeOnSaveModelCalc();
            this.successMessage(node, 'изменена')
          }
          else {
            this.nodesSrv.addNodeOnSaveModelCalc();
            this.successMessage(node, 'добавлена')
          }
        },
        err => {
          this.errorMessage(err.json());
        }
      );
  }

  saveChanges(nodes) {
    //TODO
    this.updatedNodes = Array.from(this.nodesSrv.getNodes())
      .filter((val) => val.updated === true);
    console.log('updatedNodes ', this.updatedNodes)
  }

  successMessage(node, action) {
    this.notificationService.add({
      severity: 'success',
      summary: 'Запись "' + node.descr + '" ' + action,
      detail: JSON.stringify(node, null, 2)
    })
  }

  errorMessage(error) {
    this.notificationService.add({
      severity: 'error',
      summary: error.cause,
      detail: error.url + '\n' + error.detail
    })
  }

}
