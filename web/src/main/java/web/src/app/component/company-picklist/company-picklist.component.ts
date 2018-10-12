import {Component, OnInit, ViewChild} from '@angular/core';
import {CompanyModel} from '../../model/company.model';
import {CompanyService} from '../../service/company.service';
import {CompanyEditComponent} from './company-edit.component';

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
