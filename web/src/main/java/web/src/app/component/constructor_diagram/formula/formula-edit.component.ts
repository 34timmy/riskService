import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {v4 as uuid} from 'uuid';

@Component({
  selector: 'app-formula-edit',
  templateUrl: './formula-edit.html'
})
export class FormulaEditComponent implements OnInit {
  formulaForm: FormGroup;
  showToggle = false;
  @Output()
  onSaveEvent = new EventEmitter();

  constructor(private formBuilder: FormBuilder) {
    // this.innerToggle = new BehaviorSubject<boolean>(false);
    // this.showToggle = this.getTheBoolean();

  }

  ngOnInit(): void {
    this.formulaForm = this.formBuilder.group(
      {
        id: [''],
        descr: ['', Validators.required],
        calculation: [''],
        formulaType: [''],
        weight: [''],
        a: [''],
        b: [''],
        c: [''],
        d: [''],
        xb: [''],
        comments: [''],
        rule_id: [''],
        model_calc_id: [''],
        node: [''],
        parent_node: [''],
        is_leaf: [''],
        model_id: [''],
        updating: false,
        creating: false,
        type: 'formula'
      }
    );
  }

  fillFormulaForm(formula) {
    this.formulaForm.patchValue({
      id: formula.id,
      guid:[''],
      descr: formula.descr,
      calculation: formula.calculation,
      formulaType: formula.formulaType,
      weight: formula.weight,
      a: formula.a,
      b: formula.b,
      c: formula.c,
      d: formula.d,
      xb: formula.xb,
      comments: formula.comments,
      model_calc_id: formula.model_calc_id,
      updating: false,
      creating: false,
      type: formula.type
    });
  }

  fillFormulaFormWithRuleId(rule) {
    console.log('fillFormulaFormWithRuleId', rule);
    this.formulaForm.patchValue({
      rule_id: rule.id,
      updating: false,
      type: 'formula'
    });
  }

  fillFormulaFormWithModelCalcId(modelcalc) {
    console.log('fillFormulaFormWithRuleId', modelcalc);
    let guid = uuid();
    this.formulaForm.patchValue({
      id: guid,
      guid: guid,
      descr: modelcalc.data.descr,
      model_id: modelcalc.data.model_id,
      parent_node: modelcalc.data.node,
      weight: modelcalc.data.weight,
      is_leaf: true,
      model_calc_id: modelcalc.guid,
      updating: false,
      creating: true,
      type: 'formula'
    });
  }

  onSaveFormula() {
    console.log('formula value ', this.formulaForm.value);
    this.formulaForm.value.updating = true;
    this.onSaveEvent.emit(this.formulaForm.value);
    this.closeModal();
  }

  closeModal() {
    // this.setTheBoolean(false);
    this.showToggle = false;
  }

  resetForm() {
    this.formulaForm.reset();
  }
}
