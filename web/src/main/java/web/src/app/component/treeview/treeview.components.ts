import {Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {TreeNode} from 'primeng/api';
import {TreeService} from '../../service/tree.service';
import {FormulaEditComponent} from '../formula/formula-edit.component';
import {Observable, of} from "rxjs";
import {Draggable, Droppable} from "primeng/primeng";

@Component({
  selector: 'app-treeview',
  templateUrl: 'treeview.html'
  // directives: [Draggable,Droppable]
})
export class TreeViewComponent implements OnInit {

  treemodels: TreeNode[];
  cols: any[];
  selectedNode: TreeNode;
  modelsLoaded: Observable<boolean>;
  draggedNode: TreeNode;

  constructor(private treeService: TreeService) {


  }

  @ViewChild(FormulaEditComponent)
  private formulaEditChild: FormulaEditComponent;

  ngOnInit() {
    this.treeService.getTheBoolean().subscribe(value => {
      this.modelsLoaded = of(value);
    });
    this.treemodels = this.treeService.getModelsAndConvert();
    this.cols = [
      {field: 'id', header: 'Id'},
      {field: 'name', header: 'Name'},
      {field: 'actions', header: 'Actions'}
    ];
  }

  reloadTree() {
    this.treemodels = this.treeService.getModelsAndConvert();
  }

  onEdit(node) {
    //TODO Check type
    if (node.name.toLowerCase().includes('model')) {
      this.showModelCreateDialog(node);
    }
    if (node.name.toLowerCase().includes('formula')) {
      this.showFormulaEditDialog();
      this.formulaEditChild.fillFormulaForm(node);
    }
  }

  onAdd(node) {
    //TODO Check type
    if (node.name.toLowerCase().includes('model')) {
      this.showModelCreateDialog(node);
    }
    if (node.name.toLowerCase().includes('formula')) {
      this.showFormulaEditDialog();
      this.formulaEditChild.fillFormulaFormWithRuleId(node);

    }
  }

  //-------------------------------------------------------------------------------
  onDragEnd(event, child) {
    this.treemodels.splice(this.treemodels.indexOf(child), 1);
  }

  onDrop(event, node) {
    node.subNodes = [...node.children, this.selectedNode];
  }

  onNodeDrop(event) {

  }

  dragStart(event, car) {
    // this.draggedNode = car;


  }

  drop(event) {
    // if (this.draggedNode) {
    //   let draggedCarIndex = this.findIndex(this.draggedNode);
    //   this.selectedCars = [...this.selectedCars, this.draggedNode];
    //   this.availableCars = this.availableCars.filter((val, i) => i != draggedCarIndex);
    //   this.draggedCar = null;
    // }

  }

  dragEnd(event) {
    // this.draggedCar = null;


  }

  // findIndex(car: TreeNode) {
  //   let index = -1;
  //   for (let i = 0; i < this.treemodels.length; i++) {
  //     if (car.vin === this.availableCars[i].vin) {
  //       index = i;
  //       break;
  //     }
  //   }
  //   return index;
  // }

  //------------------------------------------------------------------------------------
  private addNodeToParent(rowData) {
    this.onAdd(rowData);
    // let node: TreeNode = {
    //   parent: rowData.parent,
    //   data: {id: rowData.id, name: rowData.name},
    //   children: rowData.children
    // };
    // let newNode: TreeNode = this.onEdit(rowData);
    // if (node.parent !== undefined) {
    //   node.parent.children.push(node);
    //   console.log('toParentNode', this.selectedNode.children)
    // }
    // else {
    //   this.treemodels.push(node);
    //   console.log('no parent', this.treemodels)
    // }
  }

  private addNodeToChildren(rowData) {
    this.onAdd(rowData);
    // node = this.selectedNode;
    // console.log(node);
    // let newNode: TreeNode = {
    //   data: {id: 99, name: 'new'},
    //   children: []
    // };
    // if (node.children !== undefined) {
    //   node.children.push(newNode);
    //   console.log('toChildrenNode', this.selectedNode.children)
    // }
    // else {
    //   node.parent.children.push(newNode);
    //   console.log('no children', node.parent.children)
    // }
  }

  private showModelCreateDialog(node) {
    //TODO
  }

  private showFormulaEditDialog() {
    this.formulaEditChild.resetForm();
    this.formulaEditChild.showToggle = true;
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
