import {Injectable} from '@angular/core';
import {basePath} from "../shared/config";
import {Http} from "@angular/http";
import {HttpHeaders, HttpParams} from "@angular/common/http";

@Injectable()
export class UploadService {

  constructor(private http: Http) {
  }

  // file from event.target.files[0]
  uploadFile(file) {

    let formData = new FormData();
      formData.append('file', file);

    let params = new HttpParams();
    let headers=new HttpHeaders();
    headers.set('Content-Type', 'multipart/form-data')

    const options = {
      params: params,
      reportProgress: true
    };

    let url = basePath + "/import" + "/fromExcel";
    // const req = new HttpRequest('POST', url, formData, options);
    return this.http.post(url,formData);
  }
}
