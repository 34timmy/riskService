import {Http, Response} from '@angular/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {map} from 'rxjs/operators';
import {of} from 'rxjs';
import {
  reqOptions,
  basePath,
  reqOptionsJson,
  companiesPath,
  constructorPath,
  companyLists, modelPath
} from '../shared/config';
import {CompanyModel} from '../model/company.model';


@Injectable()
export class CompanyService {

  listsLoaded = new BehaviorSubject<boolean>(false);


  constructor(private http: Http) {
  }


  getCompanies(): Observable<Response> {
    return this.http.get(basePath + companiesPath, reqOptions);
  }

  delete(company: CompanyModel): Observable<Response> {
    return this.http.delete(basePath + companiesPath + '/' + company.id, reqOptions);
  }

  saveCompany(company: CompanyModel): Observable<Response> {
    if (company.id) {
      return this.updateCompany(company);
    } else {
      return this.createCompany(company);
    }
  }

  //
  // changeActiveStatus(company: CompanyModel): Observable<Response> {
  //     return this.http.patch(basePath + companiesPath + '/' + company.id + '/' + company.enabled, null);
  // }

  private updateCompany(company: CompanyModel): Observable<Response> {
    return this.http.put(basePath + companiesPath + '/' + company.id, JSON.stringify(company), reqOptionsJson);
  }

  private createCompany(company: CompanyModel): Observable<Response> {

    return this.http.post(basePath + companiesPath, JSON.stringify(company), reqOptionsJson);
  }

  createCompanyList(companiesList): Observable<Response> {
    //TODO create PAth
    return this.http.post(basePath + constructorPath + modelPath + companiesList, JSON.stringify(companiesList), reqOptionsJson);
  }

  updateCompanyList(companiesList): Observable<Response> {
//TODO create PAth
    return this.http.put(basePath + "", JSON.stringify(companiesList), reqOptionsJson);
  }

  getAllCompanyLists(): Observable<Response> {
    return this.http.get(basePath + constructorPath + modelPath + companyLists, reqOptions);
  }

  isListsLoaded() {
    return this.listsLoaded.asObservable();
  }

  setListsLoaded(boolean) {
    this.listsLoaded.next(boolean);
  }
}
