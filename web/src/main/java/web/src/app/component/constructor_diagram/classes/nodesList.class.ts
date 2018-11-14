import {TreeDiagramNode} from './node.class';
import {TreeDiagramNodeMaker} from "./node-maker.class"
import {ConfirmationService, MessageService} from "primeng/api";
import {ViewChild} from "@angular/core";
import {FormulaEditComponent} from "../../formula/formula-edit.component";
import {ModelEditComponent} from "../../model/model-edit.component";
import {RuleEditComponent} from "../../rule/rule-edit.component";
import {ModelcalcEditComponent} from "../../modelcalc/modelcalc-edit.component";

export class TreeDiagramNodesList {
  private _nodesList = new Map();
  public roots: TreeDiagramNode[];
  public makerGuid: string;
  public draggingNodeGuid;
  private confirmationService: ConfirmationService;
  public notificationService: MessageService;
  private _nodeTemplate = {
    displayName: 'New node',
    updated: false,
    type: '',
    children: [],
    guid: '',
    parentId: null,
    data: {}
  };
  globalNode;

  @ViewChild(FormulaEditComponent)
  private formulaEditChild: FormulaEditComponent;
  @ViewChild(RuleEditComponent)
  private ruleEditChild: RuleEditComponent;
  @ViewChild(ModelEditComponent)
  private modelEditChild: ModelEditComponent;
  @ViewChild(ModelcalcEditComponent)
  private modelCalcEditChild: ModelcalcEditComponent;

  private uuidv4() {
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function (c) {
      var r = Math.random() * 16 | 0, v = c == 'x' ? r : (r & 0x3 | 0x8);
      return v.toString(16);
    });
  }

  constructor(_nodes: any[], private config) {
    _nodes.forEach(_node => {
      this._nodesList.set(_node.guid, new TreeDiagramNode(_node, config, this.getThisNodeList.bind(this)))
    });

    this._makeRoots()
    //TODO - guid generation
    this.makerGuid = this.uuidv4()
    let node = {
      guid: this.makerGuid,
      parentId: 'root',
      children: [],
      displayName: 'New node'
    }
    let maker = new TreeDiagramNodeMaker(node, this.config, this.getThisNodeList.bind(this))
    this._nodesList.set(this.makerGuid, maker)
    this.confirmationService = this.config.confirmationService;
    this.notificationService = this.config.notificationService;
    this.formulaEditChild = this.config.formulaEditChild;
    this.ruleEditChild = this.config.ruleEditChild;
    this.modelEditChild = this.config.modelEditChild;
    this.modelCalcEditChild = this.config.modelCalcEditChild;
  }

  private _makeRoots() {
    this.roots = Array.from(this.values()).filter((node: TreeDiagramNode) => node.isRoot())

  }

  public values() {
    return this._nodesList.values()
  }

  public getNode(guid: string): TreeDiagramNode {
    return this._nodesList.get(guid)
  }

  public rootNode(guid: string) {
    let node = this.getNode(guid)
    node.isDragging = false
    node.isDragover = false
    if (node.parentId) {
      let parent = this.getNode(node.parentId)
      parent.children.delete(guid)
    }
    node.parentId = null
    this._makeRoots()
    let maker = this.getNode(this.makerGuid)
    maker.isDragging = false
    maker.isDragover = false
  }

  public transfer(origin: string, target: string) {
    let _origin = this.getNode(origin);
    let _target = this.getNode(target);
    _origin.isDragover = false;
    _origin.isDragging = false;
    _target.isDragover = false;
    if (_origin.parentId === target || origin === target) {
      return;
    }
    let remakeRoots = _origin.isRoot();
    if (_origin.parentId) {
      let _parent = this.getNode(_origin.parentId);
      _parent.children.delete(origin);
      if (!_parent.hasChildren()) {
        _parent.toggle(false)
      }
    }
    _target.children.add(origin);

    _origin.parentId = target;
    remakeRoots && this._makeRoots()

    this.serialize()
  }

  public getThisNodeList() {
    return this;
  }

  public toggleSiblings(guid: string) {
    let target = this.getNode(guid);
    if (target.parentId) {
      let parent = this.getNode(target.parentId)
      parent.children.forEach((nodeGuid) => {
        if (nodeGuid === guid) {
          return;
        }
        this.getNode(nodeGuid).toggle(false)
      })
    } else {
      for (let root of this.roots) {
        if (root.guid === guid) {
          continue;
        }
        root.toggle(false)
      }
    }
  }

  public serialize() {
    let out = []
    this._nodesList.forEach((node: TreeDiagramNode) => {
      let json: any = {
        guid: node.guid,
        displayName: node.displayName,
        parentId: node.parentId
      };
      json.children = Array.from(node.children)

      out.push(json)
    })
    return out
  }


  public destroy(node) {
    this.confirmationService.confirm({
      message: 'Are you sure that you want to perform this action?',
      accept: () => {
        if (node.type === 'formula') {
          node.treeService.deleteFormula(node.data).subscribe((val) => {
              if (val.ok) {
                this.delete(node);
                this.successMessage(node,
                  'Запись ' + node.descr + ' удалена',
                  null)
              }
            },
            err => {
              this.errorMessage(err.json())
            }
          );
        }
        else if (node.type === 'modelcalc') {
          node.treeService.deleteRule(node.data).subscribe((val) => {
              if (val.ok) {
                this.delete(node);
                this.successMessage(node,
                  'Записб ' + node.descr + ' удалена',
                  null)
              }
            },
            err => {
              this.errorMessage(err.json())
            }
          );
        }
        else if (node.type === 'model') {
          node.treeService.deleteModel(node.data).subscribe((val) => {
              if (val.ok) {
                this.delete(node);
                this.successMessage(node,
                  'Запись ' + node.descr + ' удалена',
                  null)
              }
            },
            err => {
              this.errorMessage(err.json())
            }
          );
        }
      }
    })
  }

  private delete(node) {
    let target = this.getNode(node.guid);
    if (target.parentId) {
      let parent = this.getNode(target.parentId)
      parent.children.delete(node.guid)
    }
    if (target.hasChildren()) {
      target.children.forEach((child: string) => {
        let theNode = this.getNode(child)
        theNode.parentId = null;
      })
    }
    this._nodesList.delete(node.guid)
    this._makeRoots()
    console.warn(this.values())
  }

  public edit(node) {
    this.globalNode = node;
    if (node.type === 'model') {
      this.showModelEditDialog();
      this.modelEditChild.fillModelForm(node);

    }
    else if (node.type === 'modelcalc') {
      this.showModelCalcEditDialog();
      this.modelCalcEditChild.fillModelcalcForm(node.data);
    }
    else if (node.type === 'formula') {
      this.showFormulaEditDialog();
      this.formulaEditChild.fillFormulaForm(node.data);
    }

  }

  public addNode(node = null) {
    this.globalNode = node;
    let valueForm;
    let newNodeGuid;
    console.log('Метод для добавления записи ', node);
    if (node === null) {
      this.showModelEditDialog();
      this.modelEditChild.fillEmptyModelForm();
    }
    else if (node.type.equals('model')) {
      this.showModelCalcEditDialog();
      this.modelCalcEditChild.fillModelcalcFormWithModelId(node);
    }
    else if (node.type.equals('modelcalc')) {
      this.showFormulaEditDialog();
      this.formulaEditChild.fillFormulaFormWithModelCalcId(node);
    }
  }

  public newNode(node = null, value = null) {
    let _nodeTemplate = Object.assign({}, this._nodeTemplate);
    _nodeTemplate.guid = this.uuidv4();
    //TODO guid must be id from DB
    _nodeTemplate.parentId = node ? node.guid : null;
    _nodeTemplate.displayName = value.descr;
    _nodeTemplate.updated = value.updated;
    _nodeTemplate.type = value.type;
    this._nodesList.set(_nodeTemplate.guid, new TreeDiagramNode(_nodeTemplate, this.config, this.getThisNodeList.bind(this)))
    this._makeRoots();
    return _nodeTemplate.guid

  }

  private showFormulaEditDialog() {
    this.formulaEditChild.resetForm();
    this.formulaEditChild.showToggle = true;
  }

  private showRuleEditDialog() {
    this.ruleEditChild.resetForm();
    this.ruleEditChild.showToggle = true;
  }

  private showModelCalcEditDialog() {
    this.modelCalcEditChild.resetForm();
    this.modelCalcEditChild.showToggle = true;
  }

  private showModelEditDialog() {
    this.modelEditChild.resetForm();
    this.modelEditChild.showToggle = true;
  }

  addNodeOnSaveFormula() {
    let newNodeGuid = this.newNode(this.globalNode, this.formulaEditChild.formulaForm.value)
    this.globalNode.children.add(newNodeGuid);
    this.globalNode.toggle(true)
  }

  editNodeOnSaveFormula() {
    let formValue = this.formulaEditChild.formulaForm.value;
    this.globalNode.displayName = formValue.descr;
    this.globalNode.data = formValue;

  }

  addNodeOnSaveRule() {
    let newNodeGuid = this.newNode(this.globalNode, this.ruleEditChild.ruleForm.value)
    this.globalNode.children.add(newNodeGuid);
    this.globalNode.toggle(true)
  }

  editNodeOnSaveRule() {
    let formValue = this.ruleEditChild.ruleForm.value;
    this.globalNode.displayName = formValue.name;
    this.globalNode.data = formValue;
  }

  addNodeOnSaveModelCalc() {
    let newNodeGuid = this.newNode(this.globalNode, this.modelCalcEditChild.modelcalcForm.value)
    this.globalNode.children.add(newNodeGuid);
    this.globalNode.toggle(true)
  }

  editNodeOnSaveModelCalc() {
    let formValue = this.modelCalcEditChild.modelcalcForm.value;
    this.globalNode.displayName = formValue.descr;
    this.globalNode.data = formValue;
  }

  addNodeOnSaveModel() {
    this.newNode(this.modelEditChild.modelForm.value)
  }


  editNodeOnSaveModel() {
    let formValue = this.modelEditChild.modelForm.value;
    this.globalNode.displayName = formValue.descr;
    this.globalNode.data = formValue;
  }

  successMessage(obj, summary, detail) {
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
}
