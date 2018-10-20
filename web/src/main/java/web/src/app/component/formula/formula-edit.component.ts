import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {BehaviorSubject, Observable, of} from "rxjs";

@Component({
  selector: 'app-formula-edit',
  templateUrl: './formula-edit.html'
})
export class FormulaEditComponent implements OnInit {
  formulaForm: FormGroup;
  showToggle= false;
  // innerToggle: BehaviorSubject<boolean>;
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
    // this.setTheBoolean(false);
    this.showToggle = false;
  }

  //
  // getTheBoolean(): Observable<boolean> {
  //   console.log('getTheBoolean = ', this.innerToggle)
  //   return this.innerToggle.asObservable();
  // }
  //
  // setTheBoolean(newValue: boolean): void {
  //   console.log('setTheBoolean = ', newValue)
  //   this.innerToggle.next(newValue);
  // }

  resetForm() {
    this.formulaForm.reset();
  }


}
