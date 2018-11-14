import {Component, OnInit, ViewChild} from '@angular/core';
import {CompanyModel} from '../../model/company.model';
import {CompanyService} from '../../service/company.service';
import {CompanyEditComponent} from './company-edit.component';
import {SelectItem} from "../../../../node_modules/primeng/api";
import {CompanySaveComponent} from "./company-save.component";
import {CompanyListComponent} from "./company-list.component";

@Component({
  templateUrl: './company-picklist.html',
  selector: 'app-company-picklist'
})
export class CompanyPicklistComponent implements OnInit {

  source: CompanyModel[];

  target: CompanyModel[];


  constructor(private companyService: CompanyService) {
  }

  @ViewChild(CompanyEditComponent)
  private companyEditChild: CompanyEditComponent;

  @ViewChild(CompanyListComponent)
  private companyListChild: CompanyListComponent;

  @ViewChild(CompanySaveComponent)
  private companySaveChild: CompanySaveComponent;

  ngOnInit() {
    this.companyService.getCompanies().subscribe(res => {
      this.source = res.json();
      this.companyListChild.setSaveChildComponent(this.companySaveChild);
      this.companyListChild.setAllCompanies(this.source);
      this.companySaveChild.setAllCompanyList(this.source);
    });
    this.target = [];
  }

  private reloadCompanies() {
    //TODO subscribe again?
    // this.companyService.getCompanies().subscribe(res => this.source = res as CompanyModel[]);
  }

  onEditCompany(company) {
    this.showCreateModal();
    this.companyEditChild.fillCompanyForm(company);
  }

  showCreateModal() {
    this.companyEditChild.resetForm();
    this.companyEditChild.showToggle = true;
  }

  showSaveModal(target) {
    this.companySaveChild.setSelectedCompanyList(target);
    this.companySaveChild.showToggle = true;
  }

  showCompanyList() {
    //TODO delete reload here
    this.companyListChild.reloadCompanyLists();
    // this.companyService.setListsLoaded(true);
    this.companyListChild.showToggle = true;
  }


  onSaveCompanyList(company) {
    //TODO
    // this.showCreateModal();
    // this.companyListChild.fillCompanyForm(company);
  }

  calculate() {
    this.companyListChild.reloadCompanyLists();
    this.companyListChild.calculationPerform = true;
    this.companyListChild.showToggle = true;

  }

  onSave(company: CompanyModel) {
    this.companyService.saveCompany(company)
      .subscribe(
        res => {
          this.reloadCompanies();
        }
      );
  }

  onDelete(company: CompanyModel) {
    this.companyService.delete(company).subscribe(
      res => {
        this.reloadCompanies();
      }
    );
  }
}
