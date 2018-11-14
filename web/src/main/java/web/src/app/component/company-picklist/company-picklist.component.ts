import {Component, OnInit, ViewChild} from '@angular/core';
import {CompanyModel} from '../../model/company.model';
import {CompanyService} from '../../service/company.service';
import {CompanyEditComponent} from './company-edit.component';
import {CompanyListComponent} from "./company-list.component";
import {CompanySaveComponent} from "./company-save.component";
import {HttpEventType, HttpResponse} from "@angular/common/http";
import {UploadService} from "../../service/upload.service";
import {FileUploader} from "ng2-file-upload";

@Component({
  templateUrl: './company-picklist.html',
  selector: 'app-company-picklist'
})
export class CompanyPicklistComponent implements OnInit {

  source: CompanyModel[];

  target: CompanyModel[];


  constructor(private companyService: CompanyService,
              private uploadService: UploadService
  ) {
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
    this.upload();
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

  public uploader: FileUploader = new FileUploader({url: '', itemAlias: 'photo'});

  selectFile(event) {
    // this.uploadFile(event);
  }

  upload()
  {
    this.uploader.onAfterAddingFile = (file) => {
      file.withCredentials = false;
    };
    this.uploader.onCompleteItem = (item: any, response: any, status: any, headers: any) => {
      console.log('ImageUpload:uploaded:', item, status, response);
      this.uploadService.uploadFile(item.file)
        .subscribe(
        event => {
          // if (event.type == HttpEventType.UploadProgress) {
          //   const percentDone = Math.round(100 * event.loaded / event.total);
          //   console.log(`File is ${percentDone}% loaded.`);
          // } else if (event instanceof HttpResponse) {
          //   console.log('File is completely loaded!');
          // }
        },
        (err) => {
          console.log("Upload Error:", err);
        }, () => {
          console.log("Upload done");
        }
      );
    };
  }
  // uploadFile(files: FileList) {
  //
  //   if (files.length == 0) {
  //     console.log("No file selected!");
  //     return
  //
  //   }
  //
  //   // let file: File = files[0];
  //
  //   this.uploadService.uploadFile(files.item(0))
  //     .subscribe(
  //       event => {
  //         if (event.type == HttpEventType.UploadProgress) {
  //           const percentDone = Math.round(100 * event.loaded / event.total);
  //           console.log(`File is ${percentDone}% loaded.`);
  //         } else if (event instanceof HttpResponse) {
  //           console.log('File is completely loaded!');
  //         }
  //       },
  //       (err) => {
  //         console.log("Upload Error:", err);
  //       }, () => {
  //         console.log("Upload done");
  //       }
  //     )
  // }
}
