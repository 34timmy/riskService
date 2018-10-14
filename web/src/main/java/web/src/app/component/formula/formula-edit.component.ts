import {Component, Output, EventEmitter, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators, FormControl} from '@angular/forms';
import {FormulaModel} from '../../model/formula.model';

@Component({
  templateUrl: './formula-edit.html',
  selector: 'app-formula-edit'
})
export class FormulaEditComponent implements OnInit {
  formulaForm: FormGroup;
  showToggle = false;
  @Output()
  onSaveEvent = new EventEmitter();

  constructor(private formBuilder: FormBuilder) {
  }

  ngOnInit(): void {
    this.formulaForm = this.formBuilder.group(
      {
        id: [''],
        name: ['', Validators.required],
        calculation: [''],
        formulaType: [''],
        a: [''],
        b: [''],
        c: [''],
        d: [''],
        xb: [''],
        comments: [''],
        rule_id: ['']
      }
    );
  }

  fillFormulaForm(formula) {
    this.formulaForm.patchValue({
      id: formula.id,
      name: formula.name,
      calculation: formula.calculation,
      formulaType: formula.formulaType,
      a: formula.a,
      b: formula.b,
      c: formula.c,
      d: formula.d,
      xb: formula.xb,
      comments: formula.comments,
      rule_id: formula.rule_id
    });
  }

  fillFormulaFormWithRuleId(formula) {
    this.formulaForm.patchValue({
      rule_id: formula.rule_id
    });
  }

  onSaveFormula() {
    console.log('formula value ', this.formulaForm.value);
    this.onSaveEvent.emit(this.formulaForm.value);
    this.formulaForm.reset();
    this.closeModal();
  }

  closeModal() {
    this.showToggle = false;
  }

  resetForm() {
    this.formulaForm.reset();
  }


}
