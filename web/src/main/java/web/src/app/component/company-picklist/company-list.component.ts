import {Component, Output, EventEmitter, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {CompanyModel} from '../../model/company.model';
import {TreeNode} from "../../../../node_modules/primeng/api";
import {Observable} from "rxjs";
import {CompanyService} from "../../service/company.service";
import {map} from "rxjs/operators";

@Component({
  templateUrl: './company-list.html',
  selector: 'app-company-list'
})
export class CompanyListComponent implements OnInit {

  showToggle = false;
  companyLists;
  allCompanies = [];
  selectedCompanies = [];
  companiesListNodes: TreeNode[];
  cols: any[];
  selectedNode: TreeNode;

  @Output()
  onSaveEvent: EventEmitter<CompanyModel> = new EventEmitter<CompanyModel>();

  constructor(private companyService: CompanyService) {
  }

  ngOnInit(): void {
    //TODO colmns
    this.cols = [
      {field: 'id', header: 'Id'},
      {field: 'name', header: 'Name'},
      {field: 'actions', header: 'Actions'}
    ];

    // this.companyLists = [{
    //   id: 1,
    //   companiesIds: ['1;2;3'],
    //   descr: 'Список 1'
    // }
    //   , {
    //     id: 2,
    //     companiesIds: ['1;2;3;4'],
    //     descr: 'Список 2'
    //   },
    //   {
    //     id: 3,
    //     companiesIds: ['1;'],
    //     descr: 'Список 11'
    //   }];

    //TODO list from service
    this.companyService.getAllCompanyLists().pipe(map(data => {
      this.companyLists = data;
    }));
    this.companiesListNodes = this.resultsWithCompanies(this.companyLists);

  }

  reloadCompanyLists() {
    // this.companyLists = [{
    //   id: 1,
    //   companiesIds: ['1;2;3'],
    //   descr: 'Список 1'
    // },
    //   {
    //     id: 2,
    //     companiesIds: ['1;2;3;4'],
    //     descr: 'Список 2'
    //   },
    //   {
    //     id: 3,
    //     companiesIds: ['1;'],
    //     descr: 'Список 11'
    //   },
    //   {
    //     id: 4,
    //     companiesIds: ['2;3'],
    //     descr: 'Список 23'
    //   }];

    this.companyService.getAllCompanyLists().pipe(
      map(
        data => {
          this.companyLists = data;
        }));
    this.companiesListNodes = this.resultsWithCompanies(this.companyLists);

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
          val => list.companiesIds.toString().split(";")
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
