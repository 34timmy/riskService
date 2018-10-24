import {Component, OnInit, ViewChild} from '@angular/core';

import {NodesListService} from './services/nodesList.service'
import {DomSanitizer} from "@angular/platform-browser";
import {TreeDiagramService} from "../../service/tree-diagram.service";
import {Observable, of} from "rxjs";
import {FormulaEditComponent} from "../formula/formula-edit.component";
import {ConfirmationService, MessageService} from "primeng/api";
import {ModelEditComponent} from "../model/model-edit.component";
import {NotificationService} from "../../shared/notification.service";
import {Message} from "../../../../node_modules/primeng/api";

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

  private _config = {
    confirmationService: this.confirmationService,
    notificationService: this.notificationService,
    treeService: this.treeService,
    formulaEditChild: this.formulaEditChild,
    ruleEditChild: this.ruleEditChild,
    modelEditChild: this.modelEditChild,
    nodeWidth: 200,
    nodeHeight: 100
  };

  private paneDragging = false
  private paneTransform
  private zoom = 1
  private paneX = 0
  private paneY = 0
  nodes;
  modelsLoaded: Observable<boolean>;

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
    let _data = this.treeService.getModelsAndConvert();
    // if (!_data || !Array.isArray(_data.json)) return
    // if (typeof _data.config === 'object') {
    //   this._config = Object.assign(this._config, _data.config)
    // }
    if (_data) {
      setTimeout(() => {
          this._config.formulaEditChild = this.formulaEditChild;
          this._config.ruleEditChild = this.ruleEditChild;
          this._config.modelEditChild = this.modelEditChild;
          this.nodes = this.nodesSrv.loadNodes(_data, this._config);
          this.treeService.setTheBoolean(true)
        }
      );
    }
    console.log('nodes in tree component', this.nodes)
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
    console.log('node ', node);
    this.treeService.saveFormula(node)
      .subscribe(
        res => {
          this.nodesSrv.addNodeOnSaveFormula();
          this.notificationService.add({
            severity: 'success',
            summary: 'Запись "' + node.name + '" добавлена',
            detail: 'detail'
          })
        },
        err => {
          let errJson = err.json();
          this.notificationService.add({
            severity: 'error',
            summary: errJson.cause,
            detail: errJson.url + '\n' + errJson.detail
          })
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
    console.log('node ', node);
    this.treeService.saveRule(node)
      .subscribe(
        res => {
          this.nodesSrv.addNodeOnSaveRule();
          this.notificationService.add({
            severity: 'success',
            summary: 'Запись "' + node.name + '" добавлена',
            detail: 'detail'
          })
        },
        err => {
          let errJson = err.json();
          this.notificationService.add({
            severity: 'error',
            summary: errJson.cause,
            detail: errJson.url + '\n' + errJson.detail
          })
        }
      );
  }

  onSaveModel(node) {
    console.log('node ', node);
    this.treeService.saveModel(node)
      .subscribe(
        res => {
          this.nodesSrv.addNodeOnSaveModel();
          this.notificationService.add({
            severity: 'success',
            summary: 'Запись "' + node.name + '" добавлена',
            detail: 'detail'
          })
        },
        err => {
          let errJson = err.json();
          this.notificationService.add({
            severity: 'error',
            summary: errJson.cause,
            detail: errJson.url + '\n' + errJson.detail
          })
        }
      );
  }

  onDeleteModel(model) {
    this.treeService.deleteModel(model).subscribe(
      res => {
      }
    );
  }
}
