import {AfterViewInit, Component, OnInit, ViewChild} from "@angular/core";
import {TreeService} from "../../service/tree.service";
import {ResultTableModel} from "../../model/result-table.model";
import {CompanyService} from "../../service/company.service";
import {TreeViewComponent} from "./treeview.components";
import {MessageService, SelectItem} from "primeng/api";
import {ResultTableComponent} from "./result-table.component";
import {Table} from "primeng/table";

@Component({
  selector: 'app-tableName',
  templateUrl: 'table-name.html'
})
export class TableNamesComponent implements OnInit, AfterViewInit {

  resultList;
  companyLists;
  selectedItem;
  cols: any[];
  years: SelectItem[];
  dateFilters: any;

  @ViewChild(ResultTableComponent)
  resultTable: ResultTableComponent;
  @ViewChild(TreeViewComponent)
  treeViewChild: TreeViewComponent;
  @ViewChild('dt')
  _table: Table;

  constructor(private treeService: TreeService,
              private companyService: CompanyService,
              private notificationService: MessageService
  ) {
  }

  ngOnInit() {
    //TODO

    this.cols = [{field: 'modelName', header: 'Модель'},
      {field: 'companyListName', header: 'Список'},
      {field: 'allCompaniesListName', header: 'Расширенный список'},
      {field: 'year', header: 'Год расчёта'},
      {field: 'date', header: 'Время расчёта'},
      {field: 'user', header: 'Исполнитель расчёта'},
    ];

    this.years = this.initYears();


    this.getCompaniesLists();
    this.isNeedToReloadNames();


  }

  ngAfterViewInit(): void {
    this.reloadNames();

  }


  private initYears() {

    return [
      {label: '2016', value: 2016},
      {label: '2015', value: 2015},
      {label: '2014', value: 2014},
      {label: '2013', value: 2013},
      {label: '2012', value: 2012},
      {label: '2011', value: 2011},
      {label: '2010', value: 2010},
    ]
  }

  reloadNames() {
    //TODO messages)succ;err
    this.treeService.getListOfResults().subscribe(res => {
        this.resultList = this.parseTableNames(res.json().data);
        this.filter()
      },
      err => {
        this.errorMessage(err.json());
      })

  }

  filter() {
    let _self = this;

    if (this.resultList) {

      this._table.filterConstraints['my'] = (value, filter): boolean => {
        // Make sure the value and the filter are Dates
        return value.getTime() == filter.getTime();
      }

      // this._table.filterConstraints['dateRangeFilter'] = (value, filter): boolean => {
      //   // get the from/start value
      //   var s = _self.dateFilters[0].getTime();
      //   var e;
      //   // the to/end value might not be set
      //   // use the from/start date and add 1 day
      //   // or the to/end date and add 1 day
      //   if (_self.dateFilters[1]) {
      //     e = _self.dateFilters[1].getTime() + 86400000;
      //   } else {
      //     e = s + 86400000;
      //   }
      //   // compare it to the actual values
      //   return value.getTime() >= s && value.getTime() <= e;
      // }
    }
  }

  private parseTableNames(resultDataMappers): ResultTableModel[] {
    //TODO
    let resultObj;
    let parsedNames = [];
    for (let result of resultDataMappers) {
      let companyListName1 = this.companyLists.find(
        x => x.data.id == result.companyListId).data.descr;
      let companyListName2 = this.companyLists.find(
        x => x.data.id == result.allCompanyListId).data.descr;
      parsedNames.push(new ResultTableModel(result.modelId,result.descr, result.companyListId, companyListName1,
        result.allCompanyListId, companyListName2, result.year, result.tableName))
    }
    return parsedNames;
  }

  private onRowSelect(event) {
    // this.treeViewChild.showTreeView(event.data);


  }


  showTreeView(event) {
    this.treeViewChild.showTreeView(event);

  }

  showTableView(event) {
    this.resultTable.showTreeView(event);

  }

  private getCompaniesLists() {
    this.companyService.companyLists.subscribe(res => {
      this.companyLists = res;
      this.resultTable.setCompanies(res);
      this.treeViewChild.setCompanies(res);
    });
  }

  private isNeedToReloadNames() {
    this.companyService.reloadNames.subscribe(res => {
      if (res) {
        this.reloadNames();
      }
    })
  }

  filterDate() {

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

}
