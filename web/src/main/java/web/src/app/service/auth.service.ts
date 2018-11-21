import {Injectable} from "@angular/core";
import {Http, Headers, RequestOptions} from "@angular/http";
import {Router} from "@angular/router";
import {UserModel} from "../model/user.model";
import {basePath, loginPath} from "../shared/config";
import {ProfileService} from "./profile.service";
import {Token} from "../model/auth.token";
import {Observable, of} from "rxjs";
import {map} from "rxjs/operators";
import {HttpClient, HttpHeaders} from "@angular/common/http";


@Injectable()
export class AuthService {

  private _authenticatedAs: UserModel = null;

  constructor(private http: HttpClient,
              private router: Router,
              private profileService: ProfileService) {
  }

  login(username: string, password: string) {
    const headers = new HttpHeaders({
      authorization: 'Basic ' + btoa(username + ':' + password)
    });
    // let headers: Headers = new Headers({'Content-Type': 'application/x-www-form-urlencoded;charset=utf-8'});
    let options = ({
      headers: headers,
      withCredentials: true
    });

    return this.http.get(basePath + loginPath,{headers:headers});
  }

  get authenticatedAs(): UserModel {
    return this._authenticatedAs;
  }

  set authenticatedAs(value: UserModel) {
    this._authenticatedAs = value;
  }

  hasAdminRole(): boolean {
    return this._authenticatedAs.roles.includes("ROLE_ADMIN");
  }

  logout() {
    this.http.get(basePath + "/logout").subscribe();
    this._authenticatedAs = null;
  }

  isAuthenticated(): Observable<boolean> {
    if (this._authenticatedAs == null) {
      this.profileService.getOwnProfile().subscribe(res => {
          this._authenticatedAs = res.json();
          return of(true);
        },
        err => {
          return of(false)
        })
    } else {
      return of(true);
    }
  }

}

