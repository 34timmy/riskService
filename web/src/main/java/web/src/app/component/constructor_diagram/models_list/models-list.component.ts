import {AfterViewInit, Component, OnInit, ViewChild} from "@angular/core";
import {map} from "rxjs/operators";
import {BehaviorSubject, Observable, of} from "rxjs";
import {MessageService, SelectItem} from "primeng/api";
import {Tree} from "../tree.component";
import {TreeService} from "../../../service/tree.service";
import {CompanyService} from "../../../service/company.service";
import {TreeDiagramService} from "../../../service/tree-diagram.service";

@Component({
  selector: 'app-models-list',
  templateUrl: 'models-list.html'
})
export class ModelsListComponent implements OnInit, AfterViewInit {

  models: any[];
  cols: any[];
  selectedModel: SelectItem[];

  @ViewChild(Tree)
  treeViewChild: Tree;

  constructor(private treeService: TreeDiagramService,
              private companyService: CompanyService,
              private notificationService: MessageService
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
