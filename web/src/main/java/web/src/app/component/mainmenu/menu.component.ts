import {Component} from "@angular/core";
import {MenuItem} from "primeng/api";
import {AuthService} from "../../service/auth.service";

@Component({
  templateUrl: 'menu.html',
  selector: 'main-menu'
})
export class MenuComponent {
  items: MenuItem[];
  constructor(public authService: AuthService) {
  }

  ngOnInit() {
    this.items = [
      {label: 'Расчёт', icon: 'fa fa-money', routerLink: ['/calculation']},
      {label: 'Результаты', icon: 'pi pi-inbox', routerLink: ['/results']},
      {label: 'Конструктор', icon: 'pi pi-inbox', routerLink: ['/constructor']},
    ];
  }
}
