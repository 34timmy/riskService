import {Http, Response} from '@angular/http';
import {Observable} from 'rxjs';
import {Injectable} from '@angular/core';
import {map} from 'rxjs/operators';
import {of} from 'rxjs';
import {reqOptions, basePath, reqOptionsJson, companiesPath} from '../shared/config';
import {ParamsModel} from '../model/params.model';


@Injectable()
export class ParamsService {


  constructor(private http: Http) {
  }



  getParams(): Observable<ParamsModel[]> {
    // return this.http.get(basePath + companiesPath, reqOptions).pipe(map(res => res.json()));
    const paramsModel = new ParamsModel(1, 'Params');
    const paramsModel1 = new ParamsModel(1, 'Params1');
    const paramsModel2 = new ParamsModel(1, 'Params2');
    const paramsModel3 = new ParamsModel(1, 'Params3');
    const paramsModels: ParamsModel[] = [paramsModel, paramsModel1, paramsModel2, paramsModel3];
    return of(paramsModels);
  }

  delete(params: ParamsModel): Observable<Response> {
    return this.http.delete(basePath + companiesPath + '/' + params.id, reqOptions);
  }

  save(params: ParamsModel): Observable<Response> {
    if (params.id) {
      return this.update(params);
    } else {
      return this.create(params);
    }
  }

  private update(params: ParamsModel): Observable<Response> {
    return this.http.put(basePath + companiesPath + '/' + params.id, JSON.stringify(params), reqOptionsJson);
  }

  private create(params: ParamsModel): Observable<Response> {

    return this.http.post(basePath + companiesPath, JSON.stringify(params), reqOptionsJson);
  }
}
