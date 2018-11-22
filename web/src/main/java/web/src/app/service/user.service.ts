import {Headers, Http, RequestOptions, Response} from "@angular/http";
import {UserModel} from "../model/user.model";
import {Injectable} from "@angular/core";
import { basePath,  registerPath, usersPath} from "../shared/config";
import {Observable} from "rxjs";
import {map} from "rxjs/operators";
import {AuthService} from "./auth.service";


@Injectable()
export class UserService {
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

    registerUser(value: UserModel): Observable<Response> {
        return this.http.post(basePath + registerPath, JSON.stringify(value), this.reqOptionsJson)
    }


    getUsers(): Observable<UserModel[]> {
        return this.http.get(basePath + usersPath, this.reqOptions).pipe(map(res => res.json()));
    }

    delete(user: UserModel): Observable<Response> {
        return this.http.delete(basePath + usersPath + '/' + user.id, this.reqOptions);
    }

    getUserId():number {
        return
}

    saveUser(user: UserModel): Observable<Response> {
        if (user.id) {
            return this.updateUser(user);
        } else {
            return this.createUser(user);
        }
    }

    changeActiveStatus(user: UserModel): Observable<Response> {
        return this.http.patch(basePath + usersPath + '/' + user.id + '/' + user.enabled, null);
    }

    private updateUser(user: UserModel): Observable<Response> {
        return this.http.put(basePath + usersPath + '/' + user.id, JSON.stringify(user), this.reqOptionsJson);
    }

    private createUser(user: UserModel): Observable<Response> {

        return this.http.post(basePath + usersPath, JSON.stringify(user), this.reqOptionsJson);
    }
}
