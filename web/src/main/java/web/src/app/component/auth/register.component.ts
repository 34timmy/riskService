import {Component, OnInit} from "@angular/core";
import {FormGroup, FormBuilder, Validators} from "@angular/forms";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";
import {first} from "rxjs/operators";
import {MessageService} from "primeng/api";
import {v4 as uuid} from 'uuid';

@Component({
  templateUrl: 'register.html'
})
export class RegisterComponent implements OnInit {
  registerForm: FormGroup;
  loading = false;
  submitted = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private userService: UserService,
    private notificationService: MessageService
  ) {
  }

  ngOnInit() {
    this.registerForm = this.formBuilder.group({
      id:[''],
      email: ['', Validators.required],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  get f() {
    return this.registerForm.controls;
  }

  onSubmit() {
    this.submitted = true;

    if (this.registerForm.invalid) {
      return;
    }

    this.registerForm.value.id=uuid ();
    this.loading = true;
    this.userService.registerUser(this.registerForm.value)
      .pipe(first())
      .subscribe(
        data => {
          this.successMessage("", 'Registration successful', "");
          this.router.navigate(['/login']);
        },
        error => {
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
