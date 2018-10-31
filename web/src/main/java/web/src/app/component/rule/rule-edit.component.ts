import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-rule-edit',
  templateUrl: './rule-edit.html'
})
export class RuleEditComponent implements OnInit {
  ruleForm: FormGroup;
  showToggle = false;
  @Output()
  onSaveEvent = new EventEmitter();

  constructor(private formBuilder: FormBuilder) {

  }

  ngOnInit(): void {
    this.ruleForm = this.formBuilder.group(
      {
        id: [''],
        name: ['', Validators.required],
        updated: false,
        type: 'rule'
      }
    );
  }

  fillRuleForm(rule) {
    console.log('fillRuleForm', rule);

    this.ruleForm.patchValue({
      id: rule.id,
      name: rule.name,
      model_id: rule.model_id,
      updated: false,
      type: 'rule'
    });
  }

  fillRuleFormWithModelId(model) {
    this.ruleForm.patchValue({
      id: '',
      name: '',
      model_id: model.id,
      updated: false,
      type: 'rule'
    });
  }

  onSaveRule() {
    console.log('rule value ', this.ruleForm.value);
    this.ruleForm.value.updated = true;
    this.onSaveEvent.emit(this.ruleForm.value);
    this.closeModal();
  }

  closeModal() {
    this.showToggle = false;
  }

  resetForm() {
    this.ruleForm.reset();
  }
}
