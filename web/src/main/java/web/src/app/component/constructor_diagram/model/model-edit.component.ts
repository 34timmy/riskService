import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {v4 as uuid} from 'uuid';

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
        guid:[''],
        descr: ['', Validators.required],
        updating: false,
        creating: false,
        type: 'model'
      }
    );
  }

  fillModelForm(model) {
    console.log('fillModelForm', model);

    this.modelForm.patchValue({
      id: model.id,
      descr: model.descr,
      parentId: model.parentId,
      updating: false,
      creating: false,
      data: model.data,
      type: model.type
    });
  }

  fillEmptyModelForm() {
    let guid = uuid();
    this.modelForm.patchValue({
      id: guid,
      guid: guid,
      descr: '',
      updating: false,
      creating: true,
      type: 'model'
    });
  }

  onSaveModel() {
    console.log('model value ', this.modelForm.value);
    // this.modelForm.value.updating = true;
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
