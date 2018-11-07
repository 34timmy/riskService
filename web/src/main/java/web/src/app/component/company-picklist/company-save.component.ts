import {Component, Output, EventEmitter, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {CompanyModel} from '../../model/company.model';
import {log} from 'util';
import {TreeNode} from "../../../../node_modules/primeng/api";
import {Observable} from "rxjs";

@Component({
  templateUrl: './company-save.html',
  selector: 'app-company-save'
})
export class CompanySaveComponent implements OnInit {
  companyForm: FormGroup;

  showToggle = false;

  companiesLists: TreeNode[];
  cols: any[];
  selectedNode: TreeNode;

  @Output()
  onSaveEvent: EventEmitter<CompanyModel> = new EventEmitter<CompanyModel>();

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.cols = [
      {field: 'id', header: 'Id'},
      {field: 'name', header: 'Name'},
      {field: 'actions', header: 'Actions'}
    ];

    this.companyForm = this.formBuilder.group(
      {
        id: [''],
        name: ['', Validators.required],
        INN: ['', Validators.required]
      }
    );
  }

  fillCompanyForm(company: CompanyModel) {
    this.companyForm.patchValue({
      id: company.id,
      name: company.name,
      INN: company.INN
    });
  }

  onSave() {
    this.onSaveEvent.emit(this.companyForm.value);
    this.companyForm.reset();
    this.closeModal();
  }

  closeModal() {
    this.showToggle = false;
  }

  resetForm() {
    this.companyForm.reset();
  }
}
