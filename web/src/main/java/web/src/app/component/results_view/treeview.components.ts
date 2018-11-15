import {Component, OnInit, ViewChild} from '@angular/core';
import {MessageService, TreeNode} from 'primeng/api';
import {TreeService} from '../../service/tree.service';
import {FormulaEditComponent} from '../formula/formula-edit.component';
import {Observable, of} from "rxjs";
import {map} from "rxjs/operators";

@Component({
  selector: 'app-treeview',
  styleUrls: ['../../resources/css/picklist.css'],
  templateUrl: 'treeview.html'
})
export class TreeViewComponent implements OnInit {

  treeCompanyListResult: TreeNode[];
  resultList;
  companies;
  items;
  cols: any[];
  selectedNode: TreeNode;
  modelsLoaded: Observable<boolean>;
  draggedNode: TreeNode;
  showToggle;

  constructor(private treeService: TreeService,
              private notificationService: MessageService) {
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
    // this.treeCompanyListResult = this.treeService.getResultTableNames(modelId, companyListId, allCompaniesListId, year);
    this.cols = [
      {field: 'inn', header: 'ИНН', width: '30%'},
      {field: 'name', header: 'Название', width: '30%'},
      {field: 'weight', header: 'Вес', width: '10%'},
      {field: 'value', header: 'Показатель риска', width: '10%'},
      {field: 'actions', header: 'Действия', width: '20%'}
    ];
  }

  reloadTree() {
    // this.treeCompanyListResult = this.treeService.getModelsAndConvert();
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
    this.treeService.getResultTableNamesRequest(modelId, companyListId, allCompaniesListId, year)
      .subscribe(
        tableName => {
          this.treeService.getCalcResultDTOs(tableName.json().data).subscribe(calcResultDTOs => {
              let calcResultDTOsJSON = calcResultDTOs.json().data;
              this.treeCompanyListResult = this.convertResultListTableNamesToTreeNode(calcResultDTOsJSON, companyListId);
              this.successMessage(calcResultDTOsJSON,
                'Запись ' + calcResultDTOsJSON + 'с иерархией расчёта получена',
                null);
            },
            err => {
              this.errorMessage(err.json())
            });
          this.successMessage(tableName,
            'Таблица '+tableName+' получена',
            '');
        }, err => {
          this.errorMessage(err.json())
        });
    this.showToggle = true;

  }

  convertResultListTableNamesToTreeNode(list, companyListId) {

    let keys = Object.keys(list);
    return keys.map(key => {
        console.log('treeview', this.companies
          .filter(x => x.data.id == companyListId)
          .find(x => x.children.data ? x.children.data.id.toString() == key : false))
        return {
          data: {
            id: key,
            descr: this.companies
              .filter(x => x.data.id == companyListId)
              .find(x => {
                  for (let obj of x.children) {
                    if (obj.data) {
                      if (obj.data.id.toString() == key) {
                        return obj;
                      }
                    }
                  }
                }
              )
              .data.descr
          },
          children: [this.convertChildren(list[key])]
        }
      }
    )


  }


  convertChildren(list) {
    //TODO
    let convertedObj;
    let tempList = [];
    if (list.length == undefined) {
      convertedObj = {
        data: {
          comment: list.comment,
          companyId: list.companyId,
          isLeaf: list.isLeaf,
          node: list.node,
          parentNode: list.parentNode,
          value: list.value,
          weight: list.weight
        },
        children: tempList
      };
    }
    else {
      list.forEach(val =>
        this.convertChildren(val))
    }
    if (list.children) {
      if (list.children.length == 0) {
        return convertedObj;
      }
      else {
        for (let child of list.children) {
          tempList.push(this.convertChildren(child))
        }
        return convertedObj;
      }
    }
  }

  closeModal() {
    this.showToggle = false;
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

  errorMessage(error) {
    this.notificationService.add({
      severity: 'error',
      summary: error.cause,
      detail: error.url + '\n' + error.detail
    })
  }

  setCompanies(res) {
    this.companies = res;
  }
}
