import {Component, OnInit} from "@angular/core";
import {FormGroup, FormBuilder, Validators} from "@angular/forms";
import {UserService} from "../../service/user.service";
import {Router} from "@angular/router";

@Component({
  // moduleId: module.id,
  templateUrl: "../../../templates/user/register.html"
})
export class RegisterComponent {

  model: any = {};
  loading = false;

  constructor(
    private router: Router,
    private userService: UserService) {
  }

  registerU() {
    this.loading = true;
    this.userService.registerUser(this.model)
      .subscribe(
        data => {
          // set success message and pass true paramater to persist the message after redirecting to the login page
          this.router.navigate(['/login']);
        },
        error => {

        });
  }

  // save() {
  //     this.userService.registerUser(this.registerForm.value).subscribe(
  //         res => {
  //             this.router.navigate(['/login']);
  //         },
  //         err => {
  //             this.exceptionService.onError(err);
  //         }
  //     );
  // }
}
