import {Component, EventEmitter, OnInit, Output} from "@angular/core";
import {CompanyService} from "../../service/company.service";
import {CompanyDataModel} from "../../model/company-data.model";


@Component({
  templateUrl: "data-tab.html",
  selector: "app-data-tab"
})
export class DataTabComponent implements OnInit {
  dataSB;
  cols: any[];
  company;

  @Output()
  onSave: EventEmitter<CompanyDataModel> = new EventEmitter<CompanyDataModel>();


  constructor(private companyService: CompanyService) {
  }

  ngOnInit(): void {
    this.cols = [
      {field: 'paramCode', header: 'Код'},
      {field: 'paramValue', header: 'Значение'}
    ];

  }

  onSaveData() {
    // this.onSave.emit(this.companyData)
  }

  closeModal() {
  }
}
