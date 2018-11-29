import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthService} from "../../service/auth.service";
import {Router} from "@angular/router";
import {ErrorModel} from "../../model/error.model";
import {MessageService} from "primeng/api";

@Component({
    templateUrl: 'header.html',
    selector: 'header-component'
})
export class HeaderComponent implements OnInit {

    private errors: ErrorModel[] = [];


    loginForm: FormGroup = this.formBuilder.group({
        "login": ["", Validators.required],
        "password": ["", Validators.required]
    });

    constructor(public authService: AuthService,
                private router: Router,
                private formBuilder: FormBuilder,
                private notificationService: MessageService) {
    }

    ngOnInit(): void {
    }

    onLogout() {
        this.authService.logout();
        this.router.navigate(["login"]);
    }

    onProfile()
    {
      this.router.navigate(["profile"]);

    }


}
