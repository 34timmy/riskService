import {Component, OnInit, ViewChild} from '@angular/core';
import {ParamsModel} from '../../model/params.model';
import {ParamsService} from '../../service/params.service';
import {ParamsEditComponent} from '../params/params-edit.component';

@Component({
  templateUrl: './params-picklist.html',
  selector: 'app-params-picklist'
})
export class ParamsPicklistComponent implements OnInit {

  source: ParamsModel[];

  target: ParamsModel[];

  constructor(private paramsService: ParamsService) {
  }

  @ViewChild(ParamsEditComponent)
  private paramsEditChild: ParamsEditComponent;

  ngOnInit() {
    this.paramsService.getParams().subscribe(res => this.source = res as ParamsModel[]);
    this.target = [];
  }

  showCreateModal() {
    this.paramsEditChild.resetForm();
    this.paramsEditChild.showToggle = true;
  }

  onEdit(params) {
    this.showCreateModal();
    this.paramsEditChild.fillParamsForm(params.data);
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

  private reloadParams() {
    this.paramsService.getParams().subscribe(res => this.source = res as ParamsModel[]);
  }
}
