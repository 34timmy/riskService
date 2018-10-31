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
        name: ['', Validators.required],
        updated: false,
        type: 'model'
      }
    );
  }

  fillModelForm(model) {
    console.log('fillModelForm', model);

    this.modelForm.patchValue({
      id: model.id,
      name: model.name,
      updated: false,
      type: 'model'
    });
  }

  fillEmptyModelForm() {
    this.modelForm.patchValue({
      id: '',
      name: '',
      updated: false,
      type: 'model'
    });
  }

  onSaveModel() {
    console.log('model value ', this.modelForm.value);
    this.modelForm.value.updated = true;
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
