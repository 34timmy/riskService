import {Component, ViewChild, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {CompanyEditComponent} from './company-edit.component';
import {CompanyModel} from '../../model/company.model';
import {CompanyService} from '../../service/company.service';


@Component({
  templateUrl: './company-list.html',
  selector: 'app-company-list'
})
export class CompanyListComponent implements OnInit {

  companiesHolder: Observable<CompanyModel[]>;

  @ViewChild(CompanyEditComponent)
  private companyEditChild: CompanyEditComponent;

  constructor(private companyService: CompanyService) {
  }

  ngOnInit(): void {
    this.reloadCompanies();
  }

  private reloadCompanies() {
    this.companiesHolder = this.companyService.getCompanies();
  }

  showCreateModal() {
    this.companyEditChild.resetForm();
    this.companyEditChild.showToggle = true;
  }

  onEdit(company) {
    this.showCreateModal();
    this.companyEditChild.fillCompanyForm(company.data);
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

  // onChangeActiveStatus(company: CompanyModel) {
  //     company.enabled = !company.enabled;
  //     this.companyService.changeActiveStatus(company)
  //         .subscribe(
  //             res => {
  //                 this.reloadCompanies();
  //             },
  //         );
  // }

}
