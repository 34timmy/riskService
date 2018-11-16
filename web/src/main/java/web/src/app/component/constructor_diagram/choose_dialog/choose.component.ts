import {Component, EventEmitter, OnInit, Output, ViewChild} from "@angular/core";
import {ModelcalcEditComponent} from "../modelcalc/modelcalc-edit.component";
import {FormulaEditComponent} from "../formula/formula-edit.component";

@Component({
  templateUrl: 'choose.html',
  selector: 'app-choose'
})
export class ChooseComponent implements OnInit {

  node;

  showToggle = false;

  showAgr: boolean;

  showFormula: boolean;

  @ViewChild("chooseModelCalc")
  modelCalcChild: ModelcalcEditComponent;

  @ViewChild("chooseFormula")
  formulaChild: FormulaEditComponent;

  @Output()
  onChooseAgr = new EventEmitter();
  @Output()
  onChooseFormula = new EventEmitter();

  ngOnInit(): void {
  }


  chooseAgr() {
    this.closeModal();
    this.modelCalcChild.resetForm();
    this.modelCalcChild.fillModelcalcFormWithModelId(this.node);
    this.modelCalcChild. showToggle = true;
  }

  chooseFormula() {
    this.closeModal();
    this.formulaChild.resetForm();
    this.formulaChild.fillFormulaFormWithModelCalcId(this.node);
    this.formulaChild.showToggle = true;

  }

  closeModal() {
    this.showToggle = false;
    this.showAgr = false;
    this.showFormula = false;
  }

  setModelCalcChild(modelCalcEditChild) {
    this.modelCalcChild = modelCalcEditChild;
  }

  setFormulaChild(formulaEditChild) {
    this.formulaChild = formulaEditChild;

  }
}
