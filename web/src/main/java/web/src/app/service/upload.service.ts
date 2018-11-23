import {Injectable} from '@angular/core';
import {basePath, headers} from "../shared/config";
import {Headers, Http, RequestOptions} from "@angular/http";
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {AuthService} from "./auth.service";

@Injectable()
export class UploadService {

  constructor(private http: HttpClient,
              private authenticationService: AuthService) {
  }

  uploadFile(file: File) {

    const formData = new FormData();
    formData.append('file', file, file.name);

    let params = new HttpParams();
    // const options = ;

    let url = basePath + "/import" + "/fromExcel";
    // const req = new HttpRequest('POST', url, formData, options);
    return this.http.post(url, formData, {
      headers: new HttpHeaders({
        'Authorization': 'Bearer ' + this.authenticationService.getToken()}),
      observe: 'events',
      params: params,
      reportProgress: true

    })
  }
}
