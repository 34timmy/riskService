import {Injectable} from '@angular/core';
import {basePath} from "../shared/config";
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
    let headers = new HttpHeaders();
    headers.set('Content-Type', 'multipart/form-data');
    headers.set('Authorization', 'Bearer ' + this.authenticationService.getToken());
    const options = {
      params: params,
      reportProgress: true
    };

    let url = basePath + "/import" + "/fromExcel";
    // const req = new HttpRequest('POST', url, formData, options);
    return this.http.post(url, formData, {
      reportProgress: true,
      observe: 'events',
      headers: headers
    });
  }
}
