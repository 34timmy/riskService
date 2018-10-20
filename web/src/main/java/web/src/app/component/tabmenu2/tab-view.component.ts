import {Component} from '@angular/core';
import {MessageService} from 'primeng/api';

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
}
