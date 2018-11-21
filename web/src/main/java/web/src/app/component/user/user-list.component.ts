import {Component, ViewChild, OnInit} from "@angular/core";
import {UserEditComponent} from "./user-edit.component";
import {UserModel} from "../../model/user.model";
import {UserService} from "../../service/user.service";
import {Observable} from "rxjs";



@Component({
    templateUrl: "user-list.html",
    selector: 'user-list'
})
export class UserListComponent implements OnInit {

    usersHolder: Observable<UserModel[]>;

    @ViewChild(UserEditComponent)
    private userEditChild: UserEditComponent;

    constructor(private userService: UserService
    ) {
    }

    ngOnInit(): void {
        this.reloadUsers();
    }

    private reloadUsers() {
        this.usersHolder = this.userService.getUsers();
    }

    showCreateModal() {
        this.userEditChild.resetForm();
        this.userEditChild.showToggle = true;
    }

    onEdit(user) {
        this.showCreateModal();
        this.userEditChild.fillUserForm(user.data);
    }

    onSave(user: UserModel) {
        this.userService.saveUser(user)
            .subscribe(
                res => {
                    this.reloadUsers();
                }
            );
    }

    onDelete(user: UserModel) {
        this.userService.delete(user).subscribe(
            res => {
                this.reloadUsers();
            }
        );
    }

    onChangeActiveStatus(user: UserModel) {
        user.enabled = !user.enabled;
        this.userService.changeActiveStatus(user)
            .subscribe(
                res => {
                    this.reloadUsers();
                },
            );
    }

}
