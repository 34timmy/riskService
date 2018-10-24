import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-model-edit',
  templateUrl: './model-edit.html'
})
export class ModelEditComponent implements OnInit {
  modelForm: FormGroup;
  showToggle = false;
  @Output()
  onSaveEvent = new EventEmitter();

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.modelForm = this.formBuilder.group(
      {
        id: [''],
        name: ['', Validators.required]
      }
    );
  }

  fillModelForm(model) {
    console.log('fillModelForm', model);

    this.modelForm.patchValue({
      id: model.id,
      name: model.name
    });
  }

  fillEmptyModelForm() {
    this.modelForm.patchValue({
      id: '',
      name: ''
    });
  }

  onSaveModel() {
    console.log('model value ', this.modelForm.value);
    this.onSaveEvent.emit(this.modelForm.value);
    this.closeModal();
  }

  closeModal() {
    this.showToggle = false;
  }

  resetForm() {
    this.modelForm.reset();
  }


}
