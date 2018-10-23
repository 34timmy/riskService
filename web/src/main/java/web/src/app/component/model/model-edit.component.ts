import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {BehaviorSubject, Observable, of} from "rxjs";

@Component({
  selector: 'app-model-edit',
  templateUrl: './model-edit.html'
})
export class ModelEditComponent implements OnInit {
  modelForm: FormGroup;
  showToggle = false;
  modelSaved: BehaviorSubject<boolean>;
  @Output()
  onSaveEvent = new EventEmitter();

  constructor(private formBuilder: FormBuilder) {
    // this.innerToggle = new BehaviorSubject<boolean>(false);
    // this.showToggle = this.getTheBoolean();

  }

  ngOnInit(): void {
    this.modelSaved = new BehaviorSubject<boolean>(false);
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
    this.modelSaved.next(true);
    this.modelForm.reset();
    this.closeModal();
  }

  closeModal() {
    // this.setTheBoolean(false);
    this.showToggle = false;
  }
  resetForm() {
    this.modelForm.reset();
  }


}
