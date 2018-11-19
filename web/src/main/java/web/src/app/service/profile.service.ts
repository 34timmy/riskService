import {Http, Response} from "@angular/http";
import {UserModel} from "../model/user.model";
import {Injectable} from "@angular/core";
import {basePath, profilePath, reqOptions, reqOptionsJson} from "../shared/config";
import {Observable} from "rxjs";


@Injectable()
export class ProfileService {


    constructor(private http: Http) {
    }

    getOwnProfile(): Observable<Response> {
        return this.http.get(basePath + profilePath, reqOptions);
    }

    saveOwnProfle(value: UserModel): Observable<Response> {
        return this.http.put(basePath + profilePath, JSON.stringify(value), reqOptionsJson);
    }
}
