import {TreeDiagramNodesList} from './nodesList.class'
import {ViewChild, ViewChildren} from "@angular/core";
import {FormulaEditComponent} from "../../formula/formula-edit.component";
import {TreeDiagramService} from "../../../service/tree-diagram.service";
import {ConfirmationService} from "primeng/api";

export class TreeDiagramNode {
  public parentId: string | null;
  public guid: string;
  public width: number;
  public height: number;
  public isDragover: boolean;
  public isDragging: boolean;
  private _toggle: boolean;
  public children: Set<string>;
  public data;
  public displayName: string;
  treeService: TreeDiagramService;
  // @ViewChildren(FormulaEditComponent)
  // private formulaEditChild: FormulaEditComponent;


  constructor(props, config, public getThisNodeList: () => TreeDiagramNodesList) {

    if (!props.guid) {
      return;
    }
    for (let prop in props) {
      if (props.hasOwnProperty(prop)) {
        this[prop] = props[prop]
      }
    }

    this._toggle = false;

    if (config.nodeWidth) {
      this.width = config.nodeWidth
    }
    if (config.nodeHeight) {
      this.height = config.nodeHeight
    }
    this.children = new Set(<string[]>props.children)
    this.data = props.data;
    // this.formulaEditChild = config.formulaEditChild;
    this.treeService = config.treeService;
  }

  public edit() {
    this.getThisNodeList().edit(this);
  }

  public destroy() {
    this.getThisNodeList().destroy(this)
  }

  public get isExpanded() {
    return this._toggle
  }

  public hasChildren() {
    return !!this.children.size
  }

  public toggle(state = !this._toggle) {
    this._toggle = state;
    state && this.getThisNodeList().toggleSiblings(this.guid)
  }

  public childrenCount() {
    return this.children.size
  }

  public isRoot() {
    return this.parentId == null;
  }

  public dragenter(event) {
    event.dataTransfer.dropEffect = 'move';
  }

  public dragleave(event) {
    this.isDragover = false;
  }

  public dragstart(event) {
    event.dataTransfer.effectAllowed = 'move';
    this.isDragging = true;
    this.toggle(false)
    this.getThisNodeList().draggingNodeGuid = this.guid
  }

  public dragover(event) {
    event.preventDefault();
    if (!this.isDragging) {
      this.isDragover = true;
    }
    event.dataTransfer.dropEffect = 'move'
    return false;
  }

  public dragend(event) {
    this.isDragover = false;
    this.isDragging = false;
  }

  public drop(event) {
    event.preventDefault();
    let guid = this.getThisNodeList().draggingNodeGuid
    this.getThisNodeList().transfer(guid, this.guid)
    console.log('this node list ', this.getThisNodeList());
    return false;
  }

  public addChild() {
    console.log('addChild method called', this);
    this.getThisNodeList().addNode(this);
  }

}
