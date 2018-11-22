import {Injectable} from "@angular/core";
import {Headers, Http, Response} from "@angular/http";
import {Router} from "@angular/router";
import {UserModel} from "../model/user.model";
import {basePath, loginPath} from "../shared/config";
import {ProfileService} from "./profile.service";
import {Observable, of} from "rxjs";
import {map} from "rxjs/operators";


@Injectable()
export class AuthService {

  private _authenticatedAs: UserModel = null;

  constructor(private http: Http,
              private router: Router) {
  }

  login(username: string, password: string) {
    let headers = new Headers({'Content-Type': 'application/json'});
    return this.http
      .post(basePath + loginPath
        , JSON.stringify({username: username, password: password})
        , {headers: headers})
      .pipe(map((response: Response) => {
        let token = response.json() && response.json().token;
        if (token) {
          localStorage.setItem('currentUser', JSON.stringify({username: username, token: token}));

          // return true to indicate successful login
          return true;
        } else {
          // return false to indicate failed login
          return false;
        }
      }));
  }


  getToken(): String {
    var currentUser = JSON.parse(localStorage.getItem('currentUser'));
    var token = currentUser && currentUser.token;
    return token ? token : "";
  }

  isLoggedIn(): boolean {
    var token: String = this.getToken();
    return token && token.length > 0;
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

  // isAuthenticated(): Observable<boolean> {
  //   if (this._authenticatedAs == null) {
  //     this.profileService.getOwnProfile().subscribe(res => {
  //         this._authenticatedAs = res.json();
  //         return of(true);
  //       },
  //       err => {
  //         return of(false)
  //       })
  //   } else {
  //     return of(true);
  //   }
  // }

}

