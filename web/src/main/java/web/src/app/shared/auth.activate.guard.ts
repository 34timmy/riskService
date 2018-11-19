import {CanActivate, RouterStateSnapshot, ActivatedRouteSnapshot, Router} from "@angular/router";
import {AuthService} from "../service/auth.service";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";

@Injectable()
export class AuthActivateGuard implements CanActivate {


    constructor(private authService: AuthService,
                private router: Router) {
    }

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        return this.authService.isAuthenticated().map(res => {
            if (!res) {
                this.router.navigate(["login"]);
            }
            return res;
        });
    }
}
