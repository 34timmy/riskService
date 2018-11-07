import {Component, OnInit, ViewChild} from '@angular/core';
import {CompanyModel} from '../../model/company.model';
import {CompanyService} from '../../service/company.service';
import {CompanyEditComponent} from './company-edit.component';
import {SelectItem} from "../../../../node_modules/primeng/api";
import {CompanySaveComponent} from "./company-save.component";

@Component({
  templateUrl: './company-picklist.html',
  selector: 'app-company-picklist'
})
export class CompanyPicklistComponent implements OnInit {

  source: CompanyModel[];

  target: CompanyModel[];

  stages: SelectItem[];

  selectedStage: string;

  models: SelectItem[];

  selectedModel: string;

  years: SelectItem[];

  selectedYear: string;

  constructor(private companyService: CompanyService) {
    this.models =
      [{label: '1', value: '1'},
        {label: '2', value: '2'},
        {label: '3', value: '3'}];

    this.stages =
      [{label: 'Тендер', value: 1},
        {label: 'Закупка', value: 2}];

    this.years =
      [{label: '2016', value: 2016},
        {label: '2015', value: 2015}];
  }

  @ViewChild(CompanyEditComponent)
  private companyEditChild: CompanyEditComponent;

  @ViewChild(CompanySaveComponent)
  private companySaveChild: CompanySaveComponent;

  ngOnInit() {
    this.companyService.getCompanies().subscribe(res => this.source = res as CompanyModel[]);
    this.target = [];
  }

  private reloadCompanies() {
    this.companyService.getCompanies().subscribe(res => this.source = res as CompanyModel[]);
  }

  showCreateModal() {
    this.companyEditChild.resetForm();
    this.companyEditChild.showToggle = true;
  }

  onEditCompany(company) {
    this.showCreateModal();
    this.companyEditChild.fillCompanyForm(company);
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
