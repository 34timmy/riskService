import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {v4 as uuid} from 'uuid';

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
        guid:[''],
        descr: ['', Validators.required],
        model_id: [''],
        parent_id: [''],
        weight: [''],
        is_leaf: [''],
        updating: false,
        creating: false,
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
      parent_id: modelcalc.parent_id,
      weight: modelcalc.weight,
      is_leaf: modelcalc.is_leaf,
      updating: false,
      creating: false
    });
  }

  fillModelcalcFormWithModelId(model) {
    let guid = uuid();
    this.modelcalcForm.patchValue({
      node: guid,
      guid: guid,
      descr: '',
      model_id: model.guid,
      parent_id: model.guid,
      weight: '',
      is_leaf: false,
      updating: false,
      creating: true,
      type: 'modelcalc'
    });
  }

  fillModelcalcFormWithModelCalcId(modelCalc) {
    let guid = uuid();
    this.modelcalcForm.patchValue({
      node: guid,
      guid: guid,
      descr: '',
      model_id: modelCalc.data.model_id,
      parent_id: modelCalc.data.node,
      weight: '',
      is_leaf: false,
      updating: false,
      creating: true,
      type: 'modelcalc'
    });
  }

  onSaveModelcalc() {
    console.log('modelcalc value ', this.modelcalcForm.value);
    // this.modelcalcForm.value.updating = true;
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
