import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {map} from 'rxjs/operators';
import {of} from 'rxjs';
import {
  reqOptions,
  basePath,
  reqOptionsJson,
  companiesPath,
  constructorPath,
  companyLists
} from '../shared/config';
import {CompanyModel} from '../model/company.model';


@Injectable()
export class CompanyService {


  constructor(private http: Http) {
  }



  getCompanies(): Observable<CompanyModel[]> {
    // return this.http.get(basePath + companiesPath, reqOptions).pipe(map(res => res.json()));
    const companyModel = new CompanyModel(1, 'Company', '9999');
    const companyModel1 = new CompanyModel(2, '1Company', '1');
    const companyModel2 = new CompanyModel(3, '2Company', '2');
    const companyModel3 = new CompanyModel(4, '3Company', '3');
    const companyModels: CompanyModel[] = [companyModel, companyModel1, companyModel2, companyModel3];
    // return new Observable<CompanyModel[]>();
    return of(companyModels);
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

  createCompanyList(name, companies) {
    //TODO create PAth
    this.http.post(basePath + "", JSON.stringify(name, companies), reqOptionsJson);
  }

  updateCompanyList(name, companies) {
//TODO create PAth
    this.http.put(basePath + "", JSON.stringify(name, companies), reqOptionsJson);
  }

  getAllCompanyLists() {
    return this.http.get(basePath + constructorPath + companyLists, reqOptions);

  }
}
