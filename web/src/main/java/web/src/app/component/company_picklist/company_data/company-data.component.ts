import {Component, EventEmitter, OnInit, Output} from "@angular/core";
import {CompanyDataModel} from "../../../model/company-data.model";
import {CompanyService} from "../../../service/company.service";
import {BehaviorSubject} from "rxjs";


@Component({
  templateUrl: "company-data.html",
  selector: "app-company-data"
})
export class CompanyDataComponent implements OnInit {
  companyData: CompanyDataModel[];
  cols: any[];
  company;

  @Output()
  onSave: EventEmitter<CompanyDataModel> = new EventEmitter<CompanyDataModel>();

  showToggle = false;
  fromDataTab = false;
  loadingSpinner = new BehaviorSubject(false);

  constructor(private companyService: CompanyService) {
  }

  ngOnInit(): void {
    this.cols = [
      {field: 'year', header: 'Год'},
      {field: 'paramCode', header: 'Код'},
      {field: 'paramValue', header: 'Значение'}
    ];

  }

  showModal(company) {
    this.showToggle = true;
    this.loadingSpinner.next(true);
    this.company = company;
    this.companyService.getCompanyData(company.id).subscribe(res => {
      this.companyData = res.json().map(data => {
        return {
          year: data.year,
          paramCode: data.param_code,
          paramValue: data.param_value
        }
      });
      this.loadingSpinner.next(false);
    });
  }

  onSaveData() {
    // this.onSave.emit(this.companyData)
  }

  closeModal() {
    this.showToggle = false;
  }
}
