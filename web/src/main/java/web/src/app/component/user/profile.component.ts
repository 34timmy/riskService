import {Component, OnInit} from "@angular/core";
import {FormGroup, FormBuilder, Validators} from "@angular/forms";
import {ActivatedRoute} from "@angular/router";
import {AuthService} from "../../service/auth.service";
import {ProfileService} from "../../service/profile.service";


@Component({
    templateUrl: "templates/user/profile.html",
    selector: "profile"
})
export class ProfileComponent implements OnInit {


    private profileForm: FormGroup = this.formBuilder.group({
        'name': ['', Validators.required],
        'email': ['', Validators.required],
        'password': ['', Validators.required],
    });


    constructor(private formBuilder: FormBuilder,
                private profileService: ProfileService,
                private activatedRoute: ActivatedRoute,
                private authService: AuthService) {

    }

    ngOnInit(): void {
        this.profileService.getOwnProfile().subscribe(
            res => {
                let auth = res.json();
                this.authService.authenticatedAs = auth;
                this.profileForm.patchValue(auth);
            }
        )
    }

    save() {
        this.profileService.saveOwnProfle(this.profileForm.value).subscribe(
            res => this.ngOnInit()
        );
    }

}
