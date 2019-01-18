import {Component, OnInit} from '@angular/core';
import {MessageService, TreeNode} from 'primeng/api';
import {TreeService} from '../../service/tree.service';
import {Observable} from "rxjs";

@Component({
  selector: 'app-resulttable',
  // styleUrls: ['../../resources/css/picklist.css'],
  templateUrl: 'result-table.html'
})
export class ResultTableComponent implements OnInit {


  companyListResult: any[];
  tempListFilteredByLevel: any[];
  filteredList = [];
  companies;
  items;
  baseCols: any[];
  cols: any[];
  selectedItem;
  modelsLoaded: Observable<boolean>;
  showToggle;
  selectedLevel;
  levels: any[];

  constructor(private treeService: TreeService,
              private notificationService: MessageService) {
  }

  ngOnInit(): void {
    this.showToggle = false;
    // this.cols = [
    //   {field: 'inn', header: 'ИНН', width: '30%'},
    //   {field: 'descr', header: 'Название', width: '30%'},
    //   {field: 'weight', header: 'Вес', width: '10%'},
    //   {field: 'value', header: 'Значение индекса', width: '10%'},
    //   {field: 'level', header: 'Уровень', width: '10%'}]

  }

  reloadTable(level) {
    this.filteredList = [];
    this.baseCols = [
      {field: 'companyId', header: 'ИНН'},
      {field: 'descr', header: 'Название'}
    ];
    let keys = Object.keys(this.companyListResult);
    let tempCompanyListResult = this.convertResults(this.companyListResult);
    this.tempListFilteredByLevel = tempCompanyListResult.filter(node => node.level == level);
    if (this.tempListFilteredByLevel != undefined && this.tempListFilteredByLevel.length != 0) {
      let uniqueNodes = new Set(this.tempListFilteredByLevel.map(s => s.node))
      // .map(node => {
      //   return {
      //     companyId: node,
      //     node: this.tempListFilteredByLevel.find(s => s.id == node).node
      //   }
      for (let node of uniqueNodes) {
        this.baseCols.push({field: node, header: node})
      }
      // });


      keys.map(key => {
        let obj = {
          companyId: key,
          descr: tempCompanyListResult.find(s=>s.companyId==key).descr
        };
        for (let nodeName of uniqueNodes) {
          Object.defineProperty(obj,
            nodeName, {
              value: tempCompanyListResult.find(node => {return node.companyId == key && node.node == nodeName}).value,
              writable: true,
              enumerable: true,
              configurable: true
            })
        }

        this.filteredList.push(obj)
      })


    }
  }


  showTreeView(data) {
    let modelId = data.modelId;
    let companyListId = data.companyListId;
    let allCompaniesListId = data.allCompaniesListId;
    let year = data.year;
    let tableName = data.tableName;

    this.treeService.getCalcResultDTOsAsList(tableName)
      .subscribe(calcResultDTOs => {
          let calcResultDTOsJSON = calcResultDTOs.json().data;
          this.companyListResult = (calcResultDTOsJSON);
          // this.companyListResult = this.convertResults(calcResultDTOsJSON);
          // this.companyListResult = this.convertResultListTableNamesToTreeNode(calcResultDTOsJSON, companyListId);
          this.successMessage(calcResultDTOsJSON,
            'Запись ' + calcResultDTOsJSON + 'с иерархией расчёта получена',
            null);
        },
        err => {
          this.errorMessage(err.json())
        });

    this.showToggle = true;
  }

  convertResults(results) {
    let tempList = [];
    let keys = Object.keys(results);
    for (let key of keys) {
      let result = results[key];
      for (let node of result) {
        tempList.push(node)
      }
    }
    // return tempListFilteredByLevel;

    return [
      {
        companyId: "5410786860",
        descr: "Компания 1",
        node: "Степень надежности",
        value: 0.78,
        level: 1
      }, {
        companyId: "5410786860",
        descr: "Компания 1",
        node: "Надежность",
        value: 0.78,
        level: 2
      }, {
        companyId: "5410786860",
        descr: "Компания 1",
        node: "Репутационный риск",
        value: 0.25,
        level: 3
      }, {
        companyId: "5410786860",
        descr: "Компания 1",
        node: "Финансовый риск",
        value: 0.53,
        level: 3
      }, {
        companyId: "8602166992",
        descr: "Компания 2",
        node: "Степень надежности",
        value: 0.46,
        level: 1
      }, {
        companyId: "8602166992",
        descr: "Компания 2",
        node: "Надежность",
        value: 0.46,
        level: 2
      }, {
        companyId: "8602166992",
        descr: "Компания 2",
        node: "Репутационный риск",
        value: 0.29,
        level: 3
      }, {
        companyId: "8602166992",
        descr: "Компания 2",
        node: "Финансовый риск",
        value: 0.17,
        level: 3
      }


    ]
  }


  closeModal() {
    this.showToggle = false;
  }

  onRowSelect(data) {

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
