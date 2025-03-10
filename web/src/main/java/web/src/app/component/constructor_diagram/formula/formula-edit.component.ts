import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {v4 as uuid} from 'uuid';
import {SelectItem} from "primeng/api";
import {TreeDiagramService} from "../../../service/tree-diagram.service";

@Component({
  selector: 'app-formula-edit',
  styleUrls: ['../../../resources/css/formula.css'],
  templateUrl: './formula-edit.html'
})
export class FormulaEditComponent implements OnInit {
  formulaForm: FormGroup;
  showToggle = false;
  @Output()
  onSaveEvent = new EventEmitter();

  normParams;
  normParam: SelectItem;


  constructor(private formBuilder: FormBuilder,
              private treeService: TreeDiagramService) {
    // this.innerToggle = new BehaviorSubject<boolean>(false);
    // this.showToggle = this.getTheBoolean();

  }

  ngOnInit(): void {
    this.treeService.getNormParams().subscribe(res => {
      this.normParams = (<any[]>res.json()).map(param => {
        return {label: param.descr, value: param.value}
      })
    });

    this.formulaForm = this.formBuilder.group(
      {
        id: [''],
        guid: [''],
        descr: ['', Validators.required],
        calculationFormula: [''],
        formulaType: [''],
        weight: [''],
        a: new FormControl('a'),
        b: new FormControl('b'),
        c: new FormControl('c'),
        d: new FormControl('d'),
        _XB: new FormControl('_XB'),
        comments: [''],
        rule_id: [''],
        model_calc_id: [''],
        model_calc_descr:[''],
        node: [''],
        parent_id: [''],
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
      guid: [''],
      descr: formula.descr,
      calculationFormula: formula.calculationFormula,
      formulaType: formula.formulaType,
      weight: formula.weight,
      a: formula.a,
      b: formula.b,
      c: formula.c,
      d: formula.d,
      _XB: formula._XB,
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
      model_calc_descr: modelcalc.data.descr,
      model_id: modelcalc.data.model_id,
      parent_id: modelcalc.data.node,
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
    // this.formulaForm.value.updating = true;
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
