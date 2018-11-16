import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {CompanyModel} from '../../model/company.model';
import {MessageService, SelectItem, TreeNode} from "../../../../node_modules/primeng/api";
import {Observable, of} from "rxjs";
import {CompanyService} from "../../service/company.service";
import {CompanySaveComponent} from "./company-save.component";
import {TreeService} from "../../service/tree.service";
import {TreeDiagramService} from "../../service/tree-diagram.service";

@Component({
  templateUrl: './company-list.html',
  styleUrls: ['../../resources/css/picklist.css'],
  selector: 'app-company-list'
})
export class CompanyListComponent implements OnInit {
  stages: SelectItem[];
  selectedStage: string;

  models: any[];
  selectedModel: string;

  years: any[];
  selectedYear: string;

  showToggle = false;
  listsLoaded: Observable<boolean>;
  calculationPerform = false;

  companiesListNodes: TreeNode[];
  companyLists = [];

  allCompanies = [];
  selectedCompanies = [];

  cols: any[];
  selectedNode: TreeNode;
  saveChild: CompanySaveComponent;

  @Output()
  onSaveEvent: EventEmitter<CompanyModel> = new EventEmitter<CompanyModel>();

  constructor(private companyService: CompanyService,
              private treeService: TreeDiagramService,
              private notificationService: MessageService) {
  }

  ngOnInit(): void {
    //TODO colmns

    this.companyService.isListsLoaded().subscribe(val =>
      this.listsLoaded = of(val));
    this.cols = [
      {field: 'id', header: 'Id'},
      {field: 'name', header: 'Name'},
      {field: 'actions', header: 'Actions'}
    ];
    this.reloadCompanyLists();

  }

  reloadCompanyLists() {
    this.loadModels();
    this.years = this.loadYears();
    let allCompanyLists;
    allCompanyLists = this.companyService.getAllCompanyLists().subscribe(
      res => {

        let resJson = res.json();
        this.companiesListNodes = this.resultsWithCompanies(resJson);
        this.companyService.setCompaniesLists(this.companiesListNodes);
        this.saveChild.setSelectedCompanyList(resJson);
        this.companyService.setListsLoaded(true);
      },
      err => {
        this.errorMessage(err.json())
      }
    );

  }


  loadModels() {
    this.treeService.getAllModels().subscribe(res => {
      this.models = res.json().map(model => {
        return {label: model.descr, value: model.id}
      })
    }, err => {
      this.errorMessage(err.json())
    })
  }

  loadYears() {
    //TODO from service
    return [
      {label: 2016, value: 2016},
      {label: 2015, value: 2015},
      {label: 2014, value: 2014}
    ]

  }

  onEdit(data) {

  }

  onDelete(data) {

  }

  calculate() {
    //TODO change ids to ids in some sphere
    this.companyService.setFlagForreloadNames(false);
    this.companyService.calculate(this.selectedModel,
      this.selectedNode.data.id,
      this.selectedNode.children.map(x => {
        return x.data.id;
      }).join(";"),
      this.selectedYear)
      .subscribe(res => {
          this.companyService.setFlagForreloadNames(true);
          this.showToggle = false
          this.successMessage(res,
            'Расчёт завершен',
            '')

        },
        err => {
          this.errorMessage(err.json())
        });

  }

  onSaveCompanyList() {
    this.closeModal();
  }

  closeModal() {
    this.companyService.setListsLoaded(false);
    this.showToggle = false;
  }

  public setSaveChildComponent(component) {
    this.saveChild = component;
  }

  setSelectedCompanies(companies) {
    this.selectedCompanies = companies.map(x => Object.assign({}, x));
  }

  setAllCompanies(companies) {
    this.allCompanies = companies.map(x => Object.assign({}, x));
  }

  resultsWithCompanies(companyLists) {
    let companyListsWithCompanies = [];
    //TODO temo name
    for (let list of companyLists) {
      let companyObjs =
        this.allCompanies.filter(
          val => list.company_ids.toString().split(";")
            .map(Number).includes(val.id)).map(val => {
          return {data: {id: val.id, descr: "Temp Name", INN: val.inn}}
        });

      companyListsWithCompanies.push({
        data: {
          id: list.id,
          descr: list.descr,
        },
        children: companyObjs.map(x => Object.assign({}, x))
      })
    }
    return companyListsWithCompanies;
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
