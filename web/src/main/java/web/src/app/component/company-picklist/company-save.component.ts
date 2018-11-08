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
  companies;
  text: string;

  @Output()
  onSaveEvent: EventEmitter<CompanyModel> = new EventEmitter<CompanyModel>();

  constructor(private companyService: CompanyService) {
  }

  ngOnInit(): void {
  }

  search(event) {
    //TODO
    this.autoCompleteResults = ['Список 1', 'Список 2', 'Список 11']
    // this.mylookupservice.getResults(event.query).then(data => {
    //   this.results = data;
    // });
  }

  onSaveCompanyList(name) {
    console.log('text', name)
    if (this.autoCompleteResults.indexOf(name) !== -1) {
      this.companyService.updateCompanyList(name, this.companies);
    }
    else {
      this.companyService.createCompanyList(name, this.companies);
    }
  }

  setCompanyList(companies) {
    this.companies = companies;
  }
}
