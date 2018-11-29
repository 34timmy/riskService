import {Headers, Http, RequestOptions, Response} from "@angular/http";
import {UserModel} from "../model/user.model";
import {Injectable} from "@angular/core";
import {basePath, profilePath} from "../shared/config";
import {Observable} from "rxjs";
import {AuthService} from "./auth.service";


@Injectable()
export class ProfileService {

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

  getOwnProfile(): Observable<Response> {
    return this.http.get(basePath + profilePath +
      "?token=" + JSON.parse(localStorage.getItem('currentUser')).token
      , this.reqOptions);
  }

  saveOwnProfle(value: UserModel): Observable<Response> {
    return this.http.put(basePath + profilePath, JSON.stringify(value), this.reqOptionsJson);
  }
}
