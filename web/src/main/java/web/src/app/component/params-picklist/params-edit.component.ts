import {Component, Output, EventEmitter, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {ParamsModel} from '../../model/params.model';

@Component({
  templateUrl: './params-edit.html',
  selector: 'app-params-edit'
})
export class ParamsEditComponent implements OnInit {
  paramsForm: FormGroup;

  showToggle = false;
  @Output()
  SaveEvent: EventEmitter<ParamsModel> = new EventEmitter<ParamsModel>();

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.paramsForm = this.formBuilder.group(
      {
        id: [''],
        name: ['', Validators.required]
      }
    );
  }

  fillParamsForm(params: ParamsModel) {
    this.paramsForm.patchValue({
      id: params.id,
      name: params.name
    });
  }

  onSave() {
    this.SaveEvent.emit(this.paramsForm.value);
    this.paramsForm.reset();
    this.closeModal();
  }

  closeModal() {
    this.showToggle = false;
  }

  resetForm() {
    this.paramsForm.reset();
  }
}
