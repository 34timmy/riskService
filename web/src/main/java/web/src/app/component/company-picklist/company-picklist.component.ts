import {Component, OnInit, ViewChild} from '@angular/core';
import {CompanyModel} from '../../model/company.model';
import {CompanyService} from '../../service/company.service';
import {CompanyEditComponent} from './company-edit.component';
import {CompanyListComponent} from "./company-list.component";
import {CompanySaveComponent} from "./company-save.component";
import {HttpEventType, HttpResponse} from "@angular/common/http";
import {UploadService} from "../../service/upload.service";
import {FileUploader} from "ng2-file-upload";
import {NotificationService} from "../../shared/notification.service";
import {MessageService} from "primeng/api";
import {MenuItem} from "primeng/api";
import {CompanyDataComponent} from "../company-data/company-data.component";
import {CompanyDataModel} from "../../model/company-data.model";

@Component({
  templateUrl: './company-picklist.html',
  selector: 'app-company-picklist'
})
export class CompanyPicklistComponent implements OnInit {


  source: CompanyModel[];

  target: CompanyModel[];
  displayTopBar = false;
  loadValue = 0;

  constructor(private companyService: CompanyService,
              private uploadService: UploadService,
              private notificationService: MessageService
  ) {
  }

  @ViewChild(CompanyEditComponent)
  private companyEditChild: CompanyEditComponent;

  @ViewChild(CompanyListComponent)
  private companyListChild: CompanyListComponent;

  @ViewChild(CompanySaveComponent)
  private companySaveChild: CompanySaveComponent;

  @ViewChild(CompanyDataComponent)
  private companyDataChild: CompanyDataComponent;


  ngOnInit() {

    this.companyService.getCompanies().subscribe(res => {
        this.source = res.json();
        this.companyListChild.setSaveChildComponent(this.companySaveChild);
        this.companyListChild.setAllCompanies(this.source);
        this.companySaveChild.setAllCompanyList(this.source);
      },
      err => {
        this.errorMessage(err.json());
      });
    this.target = [];
    // this.upload();
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
    this.companyListChild.calculationPerform = false;
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
        },
        err => {
          this.errorMessage(err.json());

        }
      );
  }

  onDelete(company: CompanyModel) {
    this.companyService.delete(company).subscribe(
      res => {
        this.reloadCompanies();
      },
      err => {
        this.errorMessage(err.json());

      }
    );
  }

  onGetCompanyData(company) {
    this.companyDataChild.showModal(company);

  }

  public uploader: FileUploader = new FileUploader({url: '', itemAlias: 'photo'});


  selectedFile: File = null;

  uploadFile(event) {
    this.selectedFile = event.target.files[0]

    if (this.selectedFile == undefined) {
      console.log("No file selected!");
      return
    }

    this.uploadService.uploadFile(this.selectedFile)
      .subscribe(
        event => {
          if (event.type == HttpEventType.UploadProgress) {
            this.displayTopBar = true;
            const percentDone = Math.round(100 * event.loaded / event.total);
            this.loadValue = percentDone;
            this.successMessage('',
              'Файл ' + this.selectedFile.name + ' загружен',
              '');
            window.location.reload();
          } else if (event instanceof HttpResponse) {
            this.successMessage('',
              'Файл ' + this.selectedFile.name + ' загружен',
              '');
            window.location.reload();
            console.log('File is completely loaded!');
          }
        },
        (err) => {
          this.errorMessage(err.json());
        }, () => {
          console.log("Upload done");
        }
      )
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
