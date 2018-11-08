import {Component, Output, EventEmitter, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {CompanyModel} from '../../model/company.model';
import {TreeNode} from "../../../../node_modules/primeng/api";
import {Observable} from "rxjs";

@Component({
  templateUrl: './company-list.html',
  selector: 'app-company-list'
})
export class CompanyListComponent implements OnInit {

  showToggle = false;

  companiesLists: TreeNode[];
  cols: any[];
  selectedNode: TreeNode;

  @Output()
  onSaveEvent: EventEmitter<CompanyModel> = new EventEmitter<CompanyModel>();

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    //TODO colmns
    this.cols = [
      {field: 'id', header: 'Id'},
      {field: 'name', header: 'Name'},
      {field: 'actions', header: 'Actions'}
    ];

    this.companiesLists =
      [
        {
          "data": {
            id: 1,
            "name": "Список 1"
          },
          "children": [
            {
              "data": {
                id: 2,
                "name": "Company 1"
              }},
            {
              "data": {
                id:4,
                "name": "Company4 "
              }
            }
          ]
        }
      ]
  }


  onEdit(data)
  {

  }

  onDelete(data)
  {

  }
  onSaveCompanyList() {
    this.closeModal();
  }

  closeModal() {
    this.showToggle = false;
  }

}
