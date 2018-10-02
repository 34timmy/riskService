import {Component, ViewChild, OnInit} from '@angular/core';
import {Observable} from 'rxjs';
import {ParamsEditComponent} from './params-edit.component';
import {ParamsModel} from '../../model/params.model';
import {ParamsService} from '../../service/params.service';


@Component({
  templateUrl: './params-list.html',
  selector: 'app-params-list'
})
export class ParamsListComponent implements OnInit {

  paramsHolder: Observable<ParamsModel[]>;

  @ViewChild(ParamsEditComponent)
  private paramsEditChild: ParamsEditComponent;

  constructor(private paramsService: ParamsService) {
  }

  ngOnInit(): void {
    this.reloadParams();
  }

  private reloadParams() {
    this.paramsHolder = this.paramsService.getParams();
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

  onClick() {
    this.reloadParams();
  }

  // onChangeActiveStatus(params: ParamsModel) {
  //     params.enabled = !params.enabled;
  //     this.paramsService.changeActiveStatus(params)
  //         .subscribe(
  //             res => {
  //                 this.reloadParams();
  //             },
  //         );
  // }

}
