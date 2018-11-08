import {Component, Output, EventEmitter, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {CompanyModel} from '../../model/company.model';
import {CompanyService} from "../../service/company.service";

@Component({
  templateUrl: './company-save.html',
  selector: 'app-company-save'
})
export class CompanySaveComponent implements OnInit {
  showToggle = false;
  autoCompleteResults: string[];
  allCompanies = [];
  selectedCompanies = [];
  text: string;

  @Output()
  onSaveEvent: EventEmitter<CompanyModel> = new EventEmitter<CompanyModel>();

  constructor(private companyService: CompanyService) {
  }

  ngOnInit(): void {
  }

  search(event) {
    //TODO
    this.autoCompleteResults = [{
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
      }].map(val => {
      return val.descr
    })
    // this.companyService.getAllCompanyLists().pipe(map(data => {
    //   this.autoCompleteResults = data;
    // }));
  }

  onSaveCompanyList(name) {
    console.log('text', name)
    if (this.autoCompleteResults.indexOf(name) !== -1) {
      this.companyService.updateCompanyList(name, this.selectedCompanies);
    }
    else {
      this.companyService.createCompanyList(name, this.selectedCompanies);
    }
  }

  setSelectedCompanyList(companies) {
    this.selectedCompanies = companies;
  }

  setAllCompanyList(companies) {
    this.allCompanies = companies;
  }

}
