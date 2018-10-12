import {RuleModel} from './rule.model';

export class RiskModelModel {
  data?: any;
  children?: any[];


  constructor(data: any, children: RuleModel[]) {
    this.data = data;
    this.children = children;
  }
}
