import {Component, OnInit, ViewChild} from '@angular/core';
import {TreeNode} from 'primeng/api';
import {TreeService} from '../../service/tree.service';
import {FormulaEditComponent} from '../formula/formula-edit.component';
import {Observable, of} from "rxjs";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-treeview',
  templateUrl: 'treeview.html'
})
export class TreeViewComponent implements OnInit {

  treeCompanyListResult: TreeNode[];
  resultList;
  items;
  cols: any[];
  selectedNode: TreeNode;
  modelsLoaded: Observable<boolean>;
  draggedNode: TreeNode;
  showToggle;

  constructor(private treeService: TreeService) {


  }

  @ViewChild(FormulaEditComponent)
  private formulaEditChild: FormulaEditComponent;

  ngOnInit() {
    // this.treeService.getTheBoolean().subscribe(value => {
    //   this.modelsLoaded = of(value);
    // });
    this.showToggle = false;
    let modelId;
    let companyListId;
    let allCompaniesListId;
    let year;
    // this.treeCompanyListResult = this.treeService.getResultTable(modelId, companyListId, allCompaniesListId, year);
    this.cols = [
      {field: 'name', header: 'Название', width:'40%'},
      {field: 'weight', header: 'Вес',width:'15%'},
      {field: 'value', header: 'Показатель риска',width:'15%'},
      {field: 'actions', header: 'Действия',width:'30%'}
    ];
  }

  reloadTree() {
    this.treeCompanyListResult = this.treeService.getModelsAndConvert();
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
    //   this.treeCompanyListResult.push(node);
    //   console.log('no parent', this.treeCompanyListResult)
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


  showTreeView(data) {
    let modelId = data.modelId;
    let companyListId = data.companyListId;
    let allCompaniesListId = data.allCompaniesListId;
    let year = data.year;
    let resultsForList = [{
      data: {id: 1, name: 'll1'},
      children: [
        {
          data: {id: 2, name: 'll2'},
          children: [
            {
              data: {id: 3, name: 'lll3', value: '1'},
              children: [
                {
                  data: {id: 3, name: 'lll3', value: '1', weight: '2'},
                  children: []
                }]
            }
            , {
              data: {id: 4, name: '11ll', value: '1'},
              children: [
                {
                  data: {id: 3, name: 'lll3', value: '1', weight: '2'},
                  children: []
                }
                , {
                  data: {id: 4, name: '11ll', value: '1', weight: '2'},
                  children: []
                }]
            },
            {
              data: {id: 4, name: '11ll'},
              children: []
            },
            {
              data: {id: 3, name: 'lll3'},
              children: []
            }
            , {
              data: {id: 4, name: '11ll'},
              children: []
            }]
        }
      ]
    }];
    // this.treeService.getResultTable(modelId, companyListId, allCompaniesListId, year).pipe(map(data => {
    //   resultsForList = data;
    // }, err => {
    //
    // }));
    //TODO
    // this.treeCompanyListResult = this.convertResultListToTreeNode(resultsForList);
    this.treeCompanyListResult = resultsForList;
    this.showToggle = true;

  }

  convertResultListToTreeNode(list) {
    if (list.children != undefined && list.children.length != 0) {
      this.convertResultListToTreeNode(list.children);
    }
    return list.map(val => {
      return {
        data: val,
        children: val.children
      }
    })
  }

  closeModal() {
    this.showToggle = false;
  }
}
