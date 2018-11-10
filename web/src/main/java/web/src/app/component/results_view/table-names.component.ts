import {Component, OnInit, ViewChild} from "@angular/core";
import {TreeService} from "../../service/tree.service";
import {ResultTableModel} from "../../model/result-table.model";
import {CompanyService} from "../../service/company.service";
import {map} from "rxjs/operators";
import {TreeViewComponent} from "./treeview.components";

@Component({
  selector: 'app-tableName',
  templateUrl: 'table-name.html'
})
export class TableNamesComponent implements OnInit {

  resultList;
  selectedItem;

  @ViewChild(TreeViewComponent)
  treeViewChild: TreeViewComponent;

  constructor(private treeService: TreeService, private companyService: CompanyService) {
  }

  ngOnInit() {
    //TODO
    // this.resultList = this.parseTableNames(this.treeService.getListOfResults());
    let tempResult = [
      {table_name: "calc_result_1_1_1;2;3_2016_20182505"},
      {table_name: "calc_result_1_2_1;2;3_2016_20182505"},
      {table_name: "calc_result_1_3_1;2;3_2016_20182505"},
      {table_name: "calc_result_2_1_1;2;3_2016_20182505"},
      {table_name: "calc_result_2_2_1;2;3_2016_20182505"},
      {table_name: "calc_result_2_3_1;2;3_2016_20182505"},
    ]
    this.resultList = this.parseTableNames(tempResult);

  }

  private parseTableNames(tableNames): ResultTableModel[] {
    //TODO
    let resultObj;
    let companyLists = [{
      id: 1,
      companiesIds: ['1;2;3'],
      descr: 'Список 1'
    }
      , {
        id: 2,
        companiesIds: ['1;2;3;4'],
        descr: 'Список 2'
      },
      {
        id: 3,
        companiesIds: ['1;'],
        descr: 'Список 11'
      }];
    // this.companyService.getAllCompanyLists().pipe(map(data => {
    //     companyLists = data
    //   }
    //   , err => {
    //   }));
    let parsedNames = [];
    for (let tableName of tableNames) {
      let splited = tableName.table_name.toString().split("_");
      let companyListName = companyLists.find(
        x => x.id == splited[3])
        .descr;
      parsedNames.push(new ResultTableModel(splited[2], splited[3], companyListName,
        splited[4], splited[5], splited[6]))
    }
    return parsedNames;
  }

  private onRowSelect(event) {
  this.treeViewChild.showTreeView(event.data);



  }
}
