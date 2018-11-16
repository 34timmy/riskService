import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {CompanyModel} from '../../model/company.model';
import {CompanyService} from "../../service/company.service";
import {MessageService} from "primeng/api";
import {v4 as uuid} from 'uuid';

@Component({
  templateUrl: './company-save.html',
  selector: 'app-company-save'
})
export class CompanySaveComponent implements OnInit {
  showToggle = false;
  autoCompleteResults: string[];
  allCompaniesLists = [];
  selectedCompanies = [];
  text: string;


  @Output()
  onSaveEvent: EventEmitter<CompanyModel> = new EventEmitter<CompanyModel>();
  @Output()
  onSaveCompanyListEvent: EventEmitter<any> = new EventEmitter();

  constructor(private companyService: CompanyService,
              private notificationService: MessageService
  ) {
  }

  ngOnInit(): void {
  }

  search(event) {

    this.autoCompleteResults = this.allCompaniesLists.map(val => {
      return val.descr
    });
  }

  onSaveCompanyListName(name: string) {
    if (name === undefined || name === '') {
      this.errorMessage({
        cause: 'Введите название списка!',
        url: '',
        detail: ''
      })
    }
    if (this.selectedCompanies === undefined || this.selectedCompanies.length === 0) {
      this.errorMessage({
        cause: 'Список выбранных компаний пуст!',
        url: '',
        detail: ''
      })
    } else {
      let collectToCompanyList = this.collectToCompanyList(name, this.selectedCompanies);
      if (this.autoCompleteResults.indexOf(name) !== -1) {
        //TODO set id from existing lists
        this.errorMessage({
          cause: 'Данное название уже существует!',
          url: '',
          detail: ''
        })
        // this.companyService.updateCompanyList(collectToCompanyList).subscribe(
        //   res => {
        //   },
        //   err => {
        //   }
        // );

      }
      else {
        this.companyService.createCompanyList(collectToCompanyList).subscribe(
          res => {
            // this.companyListChild.reloadCompanyLists();
            this.closeModal();
            this.successMessage(collectToCompanyList,
              'Список ' + collectToCompanyList.descr + ' создан.',
              null);
          },
          err => {

            this.closeModal();
            this.errorMessage(err.json());
          }
        );
      }
    }
  }


  private collectToCompanyList(name, companies) {
    return {
      id: uuid(),
      descr: name,
      company_ids: companies.map(val => {
        return val.id
      }).toString()
    }
  }

  setSelectedCompanyList(companies) {
    this.selectedCompanies = companies.map(x => Object.assign({}, x));
  }

  setAllCompanyList(lists) {
    this.allCompaniesLists = lists.map(x => Object.assign({}, x));
  }

  closeModal() {
    this.showToggle = false;
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
