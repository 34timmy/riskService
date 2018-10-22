import {AfterViewInit, Component, OnInit, ViewChild, ViewChildren} from '@angular/core';

import {NodesListService} from './services/nodesList.service'
import {DomSanitizer} from "@angular/platform-browser";
import {TreeDiagramService} from "../../service/tree-diagram.service";
import {Observable, of} from "rxjs";
import {FormulaEditComponent} from "../formula/formula-edit.component";
import {ConfirmationService} from "primeng/api";

@Component({
  selector: 'tree-diagram',
  styleUrls: ['./tree.component.scss'],
  templateUrl: './tree.component.html',
})
export class Tree implements OnInit {
  @ViewChild('formulaChild')
  private formulaEditChild: FormulaEditComponent;

  private _config = {
    confirmationService: this.confirmationService,
    treeService: this.treeService,
    formulaEditChild: this.formulaEditChild,
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
    private confirmationService: ConfirmationService
  ) {
    this._config.treeService = this.treeService;
    this._config.confirmationService = this.confirmationService;
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
          this.nodes = this.nodesSrv.loadNodes(_data, this._config);
          this.treeService.setTheBoolean(true)
        }
      );
    }
    console.log('nodes in tree component', this.nodes)
  }

  reloadTree() {
    // this.treeService.setTheBoolean(false);
    // this.ngOnInit();
    // let _data = this.treeService.getModelsAndConvert();
    // if (_data) {
    //   setTimeout(() => {
    //       this._config.formulaEditChild = this.formulaEditChild;
    //       this.nodes = this.nodesSrv.loadNodes(_data, this._config);
    //       this.treeService.setTheBoolean(true)
    //     }
    //   );
    // }

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
          this.reloadTree();
        }
      );
  }

  onDeleteFormula(formula) {
    this.treeService.delete(formula).subscribe(
      res => {
        this.reloadTree();
      }
    );
  }
}
