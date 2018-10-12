import {ChangeDetectionStrategy, Component, OnInit, ViewChild, ViewEncapsulation} from '@angular/core';
import {TreeNode} from 'primeng/api';
import {TreeService} from '../../service/tree.service';
import {FormulaEditComponent} from '../formula/formula-edit.component';
import {ParamsModel} from '../../model/params.model';
import {SomenodeModel} from '../../model/somenode.model';
import {RiskModelModel} from '../../model/risk-model.model';
import {Observable} from "rxjs";
import {CompanyModel} from "../../model/company.model";

@Component({
  selector: 'app-treeview',
  templateUrl: 'treeview.html',
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class TreeViewComponent implements OnInit {
  treemodels: TreeNode[];
  cols: any[];
  selectedNode: TreeNode;

  constructor(private treeService: TreeService) {
  }

  @ViewChild(FormulaEditComponent)
  private formulaEditChild: FormulaEditComponent;

  ngOnInit() {
    this.reloadTree();
    this.cols = [
      {field: 'id', header: 'Id'},
      {field: 'name', header: 'Name'},
      {field: 'actions', header: 'Actions'}
    ];
  }

  reloadTree() {
    this.treemodels = this.treeService.getModelsAndConvert();
    console.log('reload Tree', this.treemodels);
  }

  onEdit(node) {
    //TODO Check type
    if (node.name.toLowerCase().includes('model')) {
      this.showModelCreateDialog(node);
    }
    if (node.name.toLowerCase().includes('formula')) {
      this.showFormulaEditDialogModal();
      this.formulaEditChild.fillFormulaForm(node);
    }
  }

  onAdd(node) {
    //TODO Check type
    if (node.name.toLowerCase().includes('model')) {
      this.showModelCreateDialog(node);
    }
    if (node.name.toLowerCase().includes('formula')) {
      this.showFormulaEditDialogModal();
      this.formulaEditChild.fillFormulaFormWithRuleId(node);

    }
  }

  // TODO
  onDelete(row) {
  }

  addNodeToParent(rowData) {
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


  private showModelCreateDialog(node) {

  }

  showFormulaEditDialogModal() {
    this.formulaEditChild.resetForm();
    this.formulaEditChild.showToggle = true;
  }

  addNodeToChildren(rowData) {
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

  //onSaveFormula.... rule..etc methods
  //return some object
  onSaveFormula(node) {
    console.log('node ', node);
    this.treeService.saveFormula(node)
      .subscribe(
        res => {
          this.reloadTree();
        }
      );
  }
}
