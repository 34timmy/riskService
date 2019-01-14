import {Component, OnInit, ViewChild} from '@angular/core';
import {MessageService, TreeNode} from 'primeng/api';
import {TreeService} from '../../service/tree.service';
import {FormulaEditComponent} from '../constructor_diagram/formula/formula-edit.component';
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
    // this.companyListResult = this.treeService.getResultTableNames(modelId, companyListId, allCompaniesListId, year);
    //TODO show names correctly
    this.cols = [
      {field: 'inn', header: 'ИНН', width: '30%'},
      {field: 'name', header: 'Название', width: '30%'},
      {field: 'weight', header: 'Вес', width: '10%'},
      {field: 'value', header: 'Значение индекса', width: '10%'},
      // {field: 'level', header: 'Уровень', width: '10%'},
      // {field: 'actions', header: 'Действия', width: '10%'}
    ];
  }

  reloadTree() {
    // this.companyListResult = this.treeService.getModelsAndConvert();
  }

  //------------------------------------------------------------------------------------


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
    let tableName = data.tableName;
    // this.treeService.getResultTableNamesRequest(modelId, companyListId, allCompaniesListId, year)
    //   .subscribe(
    //     tableName => {
    this.treeService.getCalcResultDTOs(tableName)
      .subscribe(calcResultDTOs => {
          let calcResultDTOsJSON = calcResultDTOs.json().data;
          this.treeCompanyListResult = this.convertResultListTableNamesToTreeNode(calcResultDTOsJSON, companyListId);
          this.successMessage(calcResultDTOsJSON,
            'Запись ' + calcResultDTOsJSON + 'с иерархией расчёта получена',
            null);
        },
        err => {
          this.errorMessage(err.json())
        });
    //   this.successMessage(tableName,
    //     'Таблица ' + tableName + ' получена',
    //     '');
    // }, err => {
    //   this.errorMessage(err.json())
    // });
    this.showToggle = true;

  }

  convertResultListTableNamesToTreeNode(list, companyListId) {

    let keys = Object.keys(list);
    return keys.map(key => {
        return {
          data: {
            id: key,
            descr: this.companies
              .find(x => x.data.id === companyListId)
              .children
              .find(x => {
                  // for (let obj of x.children) {
                  if (x.data) {
                    if (x.data.id.toString() == key) {
                      return true;
                    }
                  }
                  // }
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
          companyId: list.descr,
          isLeaf: list.isLeaf,
          node: list.node,
          parentNode: list.parentNode,
          value: list.value,
          normalizedValue: list.normalizedValue,
          level: list.level,
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
