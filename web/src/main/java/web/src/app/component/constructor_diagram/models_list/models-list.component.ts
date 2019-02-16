import {AfterViewInit, Component, OnInit, ViewChild} from "@angular/core";
import {map} from "rxjs/operators";
import {BehaviorSubject, Observable, of} from "rxjs";
import {ConfirmationService, MessageService, SelectItem} from "primeng/api";
import {Tree} from "../tree.component";
import {TreeService} from "../../../service/tree.service";
import {CompanyService} from "../../../service/company.service";
import {TreeDiagramService} from "../../../service/tree-diagram.service";
import {ModelEditComponent} from "../model/model-edit.component";

@Component({
  selector: 'app-models-list',
  styleUrls: ['models-list.css'],
  templateUrl: 'models-list.html'
})
export class ModelsListComponent implements OnInit, AfterViewInit {

  models: any[];
  cols: any[];
  selectedModel: SelectItem[];

  @ViewChild(ModelEditComponent)
  private modelEditChild: ModelEditComponent;

  @ViewChild(Tree)
  treeViewChild: Tree;

  constructor(private treeService: TreeDiagramService,
              private companyService: CompanyService,
              private notificationService: MessageService,
              private confirmationService: ConfirmationService
  ) {
  }

  ngOnInit() {
    this.treeViewChild.reloadModels.subscribe(val => {
      if (val) {
        this.reloadNames();
      }
    });
    this.cols = [
      {field: 'descr', header: 'Название модели'},
      {field: 'id', header: 'ID'}
    ];
  }

  ngAfterViewInit(): void {
    this.reloadNames();
  }


  reloadNames() {
    //TODO messages)succ;err
    this.treeService.getAllModelsOnly().subscribe(res => {
        this.models = res.json();
      },
      err => {
        this.errorMessage(err.json());
      })
  }

  private newModel() {
    this.treeViewChild.show(null);

  }

  onSaveModel(model) {
    this.treeService.copyModel(model).subscribe(res => {
      this.successMessage(model,
        'Запись ' + model.descr + ' скопирована',
        null);
    }, err => {
      this.errorMessage(err.json());
    })
  }

  onCopyModel(model) {
    this.modelEditChild.resetForm();
    this.modelEditChild.fillModelForm(model)
    this.modelEditChild.showToggle = true;
  }

  private onDeleteModel(model) {
    this.confirmationService.confirm({
      message: 'Are you sure that you want to perform this action?',
      accept: () => {
        this.treeService.deleteModel(model).subscribe(
          val => {
            if (val.ok) {
              this.reloadNames();
              this.successMessage(model,
                'Запись ' + model.descr + ' удалена',
                null)
            }
          },
          err => {
            this.errorMessage(err.json())
          });
      }
    });
  }

  private onRowSelect(model) {
    this.treeViewChild.show(model.data.id);
  }

  successMessage(obj, summary, detail) {
    if (detail == null) {
      this.notificationService.add({
        severity: 'success',
        summary: summary,
        detail: JSON.stringify(obj, null, 2)
      })
    } else {
      this.notificationService.add({
        severity: 'success',
        summary: summary,
        detail: detail
      })
    }
  }

  errorMessage(error) {
    this.notificationService.add({
      severity: 'error',
      summary: error.cause,
      detail: error.url + '\n' + error.detail
    })
  }

}
