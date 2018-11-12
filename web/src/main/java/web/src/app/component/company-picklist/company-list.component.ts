import {Component, Output, EventEmitter, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {CompanyModel} from '../../model/company.model';
import {TreeNode} from "../../../../node_modules/primeng/api";
import {BehaviorSubject, Observable, of} from "rxjs";
import {CompanyService} from "../../service/company.service";
import {map} from "rxjs/operators";

@Component({
  templateUrl: './company-list.html',
  selector: 'app-company-list'
})
export class CompanyListComponent implements OnInit {

  showToggle = false;
  companyLists = [];
  allCompanies = [];
  selectedCompanies = [];
  companiesListNodes: TreeNode[];
  cols: any[];
  selectedNode: TreeNode;
  listsLoaded: Observable<boolean>;

  @Output()
  onSaveEvent: EventEmitter<CompanyModel> = new EventEmitter<CompanyModel>();

  constructor(private companyService: CompanyService) {
  }

  ngOnInit(): void {
    //TODO colmns

    this.companyService.isListsLoaded().subscribe(val =>
      this.listsLoaded = of(val));
    this.cols = [
      {field: 'id', header: 'Id'},
      {field: 'name', header: 'Name'},
      {field: 'actions', header: 'Actions'}
    ];
  }

  reloadCompanyLists() {
    this.companyLists.length = 0;
    this.companyService.setListsLoaded(false);
    this.companyService.getAllCompanyLists().subscribe(
      res => {
        this.companiesListNodes = this.resultsWithCompanies(res.json());
        this.companyService.setListsLoaded(true);
      }
    );

  }


  onEdit(data) {

  }

  onDelete(data) {

  }

  onSaveCompanyList() {
    this.closeModal();
  }

  closeModal() {
    this.showToggle = false;
  }

  setSelectedCompanyList(companies) {
    this.selectedCompanies = companies;
  }

  setAllCompanyList(companies) {
    this.allCompanies = companies;
  }


  resultsWithCompanies(companyLists) {
    let companyListsWithCompanies = [];

    for (let list of companyLists) {
      let companyObjs =
        this.allCompanies.filter(
          val => list.company_ids.toString().split(";")
            .map(Number).includes(val.id)).map(val => {
          return {data: val}
        });

      companyListsWithCompanies.push({
        data: {
          id: list.id,
          descr: list.descr,
        },
        children: companyObjs
      })
    }
    return companyListsWithCompanies;
  }

}
