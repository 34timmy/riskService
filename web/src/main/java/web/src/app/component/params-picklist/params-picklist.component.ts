import {Component, OnInit, ViewChild} from '@angular/core';
import {ParamsModel} from '../../model/params.model';
import {ParamsService} from '../../service/params.service';
import {ParamsEditComponent} from './params-edit.component';
import {SelectItem} from 'primeng/api';

@Component({
  templateUrl: './params-picklist.html',
  selector: 'app-params-picklist'
})
export class ParamsPicklistComponent implements OnInit {

  stages: SelectItem[];

  selectedStage: string;

  models: SelectItem[];

  selectedModel: string;

  source: ParamsModel[];

  target: ParamsModel[];

  constructor(private paramsService: ParamsService) {
    this.models =
      [{label: '1', value: '1'},
        {label: '2', value: '2'},
        {label: '3', value: '3'}];

    this.stages =
      [{label: 'Тендер', value: 1},
        {label: 'Закупка', value: 2}];
  }

  @ViewChild(ParamsEditComponent)
  private paramsEditChild: ParamsEditComponent;

  ngOnInit() {

    this.paramsService.getParams().subscribe(res => this.source = res as ParamsModel[]);
    this.target = [];
  }

  private reloadParams() {
    this.paramsService.getParams().subscribe(res => this.source = res as ParamsModel[]);
  }

  showCreateModal() {
    this.paramsEditChild.resetForm();
    this.paramsEditChild.showToggle = true;
  }

  onEditParams(params) {
    this.showCreateModal();
    this.paramsEditChild.fillParamsForm(params);
  }

  onSave(params: ParamsModel) {
    this.paramsService.saveParams(params)
      .subscribe(
        res => {
          this.reloadParams();
        }
      );
  }

  onDelete(params: ParamsModel) {
    this.paramsService.delete(params).subscribe(
      res => {
        this.reloadParams();
      }
    );
  }
}
