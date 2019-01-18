import {AfterViewInit, Component, OnInit, ViewChild} from "@angular/core";
import {TreeService} from "../../service/tree.service";
import {ResultTableModel} from "../../model/result-table.model";
import {CompanyService} from "../../service/company.service";
import {TreeViewComponent} from "./treeview.components";
import {MessageService} from "primeng/api";
import {ResultTableComponent} from "./result-table.component";

@Component({
  selector: 'app-tableName',
  templateUrl: 'table-name.html'
})
export class TableNamesComponent implements OnInit, AfterViewInit {

  resultList;
  companyLists;
  selectedItem;


  @ViewChild(ResultTableComponent)
  resultTable: ResultTableComponent;
  @ViewChild(TreeViewComponent)
  treeViewChild: TreeViewComponent;

  constructor(private treeService: TreeService,
              private companyService: CompanyService,
              private notificationService: MessageService
  ) {
  }

  ngOnInit() {
    //TODO
    this.getCompaniesLists();
    this.isNeedToReloadNames();

  }

  ngAfterViewInit(): void {
    this.reloadNames();
  }


  reloadNames() {
    //TODO messages)succ;err
    this.treeService.getListOfResults().subscribe(res => {
        this.resultList = this.parseTableNames(res.json().data);

      },
      err => {
        this.errorMessage(err.json());
      })

  }

  private parseTableNames(resultDataMappers): ResultTableModel[] {
    //TODO
    let resultObj;
    let parsedNames = [];
    for (let result of resultDataMappers) {
      let companyListName = this.companyLists.find(
        x => x.data.id == result.companyListId).data.descr;
      parsedNames.push(new ResultTableModel(result.modelId, result.companyListId, companyListName,
        result.allCompanyListId, result.year, result.tableName))
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
