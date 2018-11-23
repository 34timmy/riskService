import {Component, OnInit} from "@angular/core";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../service/auth.service";
import {MessageService} from "primeng/api";
import {first} from "rxjs/operators";

@Component({
  templateUrl: 'auth.html',
  selector: 'auth'
})
export class AuthComponent implements OnInit {
  loginForm: FormGroup;
  loading = false;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private router: Router,
    private authenticationService: AuthService,
    private notificationService: MessageService) {
  }

  ngOnInit() {
    this.loginForm = this.formBuilder.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });

    if (this.authenticationService.isLoggedIn())
    {
      this.router.navigateByUrl("/calculation")

    }
    this.authenticationService.logout();
  }

  get f() {
    return this.loginForm.controls;
  }

  onSubmit() {

    if (this.loginForm.invalid) {
      return;
    }
    //TODO improve erroemessage
    this.loading = true;
    this.authenticationService.login(this.f.username.value, this.f.password.value)
      .subscribe(
        result => {
          if (result === true) {
            this.submitted = true;
            this.loading = false;
            this.router.navigateByUrl("/calculation")
          }
          else {
            this.errorMessage({cause: "Login failed"})
            this.loading = false;
          }
        },
        error => {
          this.submitted = false;
          this.errorMessage(error.json());
          this.loading = false;
        });
  }

  successMessage(obj, summary, detail) {
    if (detail == null) {
      this.notificationService.add({
        severity: 'success',
        summary: summary,
        detail: JSON.stringify(obj, null, 2)
      })
    } else {
      this.notificationService.add({
        severity: 'success',
        summary: summary,
        detail: detail
      })
    }
  }

  errorMessage(error) {
    this.notificationService.add({
      severity: 'error',
      summary: error.cause,
      detail: error.url + '\n' + error.detail
    })
  }
}
