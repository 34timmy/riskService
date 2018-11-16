import {Component, Output, EventEmitter, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {CompanyModel} from '../../model/company.model';
import {log} from 'util';

@Component({
  templateUrl: './company-edit.html',
  selector: 'app-company-edit'
})
export class CompanyEditComponent implements OnInit {
  companyForm: FormGroup;

  showToggle = false;
  @Output()
  onSaveEvent: EventEmitter<CompanyModel> = new EventEmitter<CompanyModel>();

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.companyForm = this.formBuilder.group(
      {
        id: [''],
        name: ['', Validators.required],
        INN: ['', Validators.required],
        updating: false,
        creating: false
      }
    );
  }

  fillCompanyForm(company: CompanyModel) {
    this.companyForm.patchValue({
      id: company.id,
      name: company.descr,
      INN: company.INN,
      updating: false,
      creating: false
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
