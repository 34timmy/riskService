import {FormulaModel} from './formula.model';

export class RuleModel {
  data?: any;
  children?: any[];

  constructor(data: any, children: FormulaModel[]) {
    this.data = data;
    this.children = children;
  }
}
