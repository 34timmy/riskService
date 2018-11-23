import {Component} from '@angular/core';
import {MenuItem, MessageService} from 'primeng/api';

@Component({
  templateUrl: './tabview.html',
  selector: 'app-tabpanel',
  providers: [MessageService]
})
export class TabViewComponent {
  constructor(private messageService: MessageService) {
  }

  data: any[];
  onTabChange(event) {
    this.messageService.add({severity: 'info', summary: 'Tab Expanded', detail: 'Index: ' + event.index});
  }

  items: MenuItem[];

  activeItem: MenuItem;

  ngOnInit() {
    this.items = [
      {label: 'Расчёт', icon: 'fa fa-money',routerLink: ['/calculation']},
      {label: 'Результаты', icon: 'pi pi-inbox',routerLink: ['/results']},
      {label: 'Конструктор', icon: 'pi pi-inbox',routerLink: ['/constructor']},
    ];

    this.activeItem = this.items[2];
  }
}
