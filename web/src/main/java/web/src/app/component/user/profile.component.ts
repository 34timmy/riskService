import {Component, OnInit} from "@angular/core";
import {FormGroup, FormBuilder, Validators} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {AuthService} from "../../service/auth.service";
import {ProfileService} from "../../service/profile.service";
import {UserModel} from "../../model/user.model";


@Component({
  templateUrl: "profile.html",
  styleUrls: ['profile.css'],
  selector: "profile"
})
export class ProfileComponent implements OnInit {


  public profileForm: FormGroup = this.formBuilder.group({
    'id':[''],
    'userName': ['', Validators.required],
    'firstName': ['', Validators.required],
    'lastName': ['', Validators.required],
    'email': ['', Validators.required],
    'password': ['', Validators.required],
  });


  constructor(private formBuilder: FormBuilder,
              private profileService: ProfileService,
              private activatedRoute: ActivatedRoute,
              public authService: AuthService) {

  }

  ngOnInit(): void {
    this.profileService.getOwnProfile().subscribe(
      res => {
        let auth = res.json() as UserModel;
        this.authService.authenticatedAs = auth;
        this.fillForm(auth);
      }
    )
  }

  fillForm(user) {
    this.profileForm.patchValue(
      {
        'id': user.id,
        'userName': user.userName,
        'firstName': user.firstName,
        'lastName': user.lastName,
        'email': user.email
      }
    )
  }

  onSave() {
    this.profileService.saveOwnProfle(this.profileForm.value).subscribe(
      res => this.ngOnInit()
    );
  }

}
