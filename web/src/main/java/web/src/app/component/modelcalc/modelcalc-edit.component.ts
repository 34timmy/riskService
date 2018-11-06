import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-modelcalc-edit',
  templateUrl: './modelcalc-edit.html'
})
export class ModelcalcEditComponent implements OnInit {
  modelcalcForm: FormGroup;
  showToggle = false;
  @Output()
  onSaveEvent = new EventEmitter();

  constructor(private formBuilder: FormBuilder) {

  }

  ngOnInit(): void {
    this.modelcalcForm = this.formBuilder.group(
      {
        node: [''],
        descr: ['', Validators.required],
        model_id: [''],
        parent_node: [''],
        weight: [''],
        is_leaf: [''],
        updated: false,
        type: 'modelcalc'
      }
    );
  }

  fillModelcalcForm(modelcalc) {
    console.log('fillModelcalcForm', modelcalc);

    this.modelcalcForm.patchValue({
      node: modelcalc.node,
      descr: modelcalc.descr,
      model_id: modelcalc.model_id,
      parent_node: modelcalc.parent_node,
      weight: modelcalc.weight,
      is_leaf: modelcalc.is_leaf,
      updated: false,
    });
  }

  fillModelcalcFormWithModelId(model) {
    this.modelcalcForm.patchValue({
      node: '',
      descr: '',
      model_id: model.id? model.id:model.data.model_id,
      parent_node: model.id? model.id:model.node,
      weight: '',
      is_leaf: true,
      updated: false,
      type: 'modelcalc'
    });
  }

  onSaveModelcalc() {
    console.log('modelcalc value ', this.modelcalcForm.value);
    this.modelcalcForm.value.updated = true;
    this.onSaveEvent.emit(this.modelcalcForm.value);
    this.closeModal();
  }

  closeModal() {
    this.showToggle = false;
  }

  resetForm() {
    this.modelcalcForm.reset();
  }
}
