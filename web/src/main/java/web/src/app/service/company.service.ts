import {Headers, Http, RequestOptions, Response} from '@angular/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {map} from 'rxjs/operators';
import {of} from 'rxjs';
import {
  basePath,
  companiesPath,
  constructorPath,
  companyLists, modelPath, companiesListsPath
} from '../shared/config';
import {CompanyModel} from '../model/company.model';
import {v4 as uuid} from 'uuid';
import {AuthService} from "./auth.service";

@Injectable()
export class CompanyService {

  listsLoaded = new BehaviorSubject<boolean>(false);

  private headersJson = new Headers({
    'Content-Type': 'application/json',
    'Authorization': 'Bearer ' + this.authenticationService.getToken()
  });

  private headers = new Headers({
    'Authorization': 'Bearer ' + this.authenticationService.getToken()
  });

  reqOptions: RequestOptions = new RequestOptions({
    withCredentials: true,
    headers: this.headers
  });

  reqOptionsJson: RequestOptions = new RequestOptions({
    withCredentials: true,
    headers: this.headersJson
  });

  constructor(private http: Http,
              private authenticationService: AuthService
  ) {
  }


  getCompanies(): Observable<Response> {
    return this.http.get(basePath + companiesPath, this.reqOptions);
  }

  delete(company: CompanyModel): Observable<Response> {
    return this.http.delete(basePath + companiesPath + '/' + company.id, this.reqOptions);
  }

  saveCompany(company: CompanyModel): Observable<Response> {
    if (company.id) {
      return this.updateCompany(company);
    } else {
      company.id = uuid();
      return this.createCompany(company);
    }
  }

  //
  // changeActiveStatus(company: CompanyModel): Observable<Response> {
  //     return this.http.patch(basePath + companiesPath + '/' + company.id + '/' + company.enabled, null);
  // }

  private updateCompany(company: CompanyModel): Observable<Response> {
    return this.http.put(basePath + companiesPath + '/' + company.id, JSON.stringify(company), this.reqOptionsJson);
  }

  private createCompany(company: CompanyModel): Observable<Response> {

    return this.http.post(basePath + companiesPath, JSON.stringify(company), this.reqOptionsJson);
  }

  createCompanyList(companiesList): Observable<Response> {
    //TODO create PAth
    return this.http.post(basePath + constructorPath + modelPath + companiesListsPath, JSON.stringify(companiesList),
      this.reqOptionsJson);
  }

  updateCompanyList(companiesList): Observable<Response> {
//TODO create PAth
    return this.http.put(basePath + "", JSON.stringify(companiesList), this.reqOptionsJson);
  }

  getAllCompanyLists(): Observable<Response> {
    return this.http.get(basePath + constructorPath + modelPath + companyLists, this.reqOptions);
  }

  isListsLoaded() {
    return this.listsLoaded.asObservable();
  }

  setListsLoaded(boolean) {
    this.listsLoaded.next(boolean);
  }

  calculate(selectedModel, selectedListId, selectedCompaniesId, selectedYear) {
    return this.http.get(basePath +
      "/perform" +
      "/calculation?" +
      "modelId=" + selectedModel +
      "&companyListId=" + selectedListId +
      "&industryCompanyListId=" + selectedCompaniesId +
      "&year=" + selectedYear
      , this.reqOptions)
  }

  calculateByIndustry(selectedModel, industryId, industryCompanyListId, selectedYear) {
    return this.http.get(basePath +
      "/perform" +
      "/calculation?" +
      "modelId=" + selectedModel +
      "&industryId=" + industryId +
      "&industryCompanyListId=" + industryCompanyListId +
      "&year=" + selectedYear
      , this.reqOptions)
  }

  companyLists = new BehaviorSubject<any[]>([]);

  setCompaniesLists(resJson) {
    this.companyLists.next(resJson.map(x => Object.assign({}, x)));
  }

  reloadNames = new BehaviorSubject<boolean>(false);

  setFlagForreloadNames(flag) {
    this.reloadNames.next(flag);
  }

  getCompanyData(id) {
    return this.http.get(basePath +
      companiesPath +
      "/companyData" +
      "?companyId=" + id,
      this.reqOptions)
  }
}
