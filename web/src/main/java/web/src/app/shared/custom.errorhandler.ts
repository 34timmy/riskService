import { ErrorHandler, Inject } from '@angular/core';
import { NotificationService } from './notification.service';


export class CustomErrorHandler implements ErrorHandler {

  constructor(@Inject(NotificationService) private notificationService: NotificationService) {
  }

  handleError(error: any): void {
    this.showErrorInConsole(error);
    setTimeout(() =>
      this.notificationService.error(error.json().Message), 1);
  }

  private showErrorInConsole(error: any) :void {
    if (console && console.group && console.error) {
      console.group("Error Log");
      console.error(error);
      console.error(error.message);
      console.error(error.stack);
      console.groupEnd();
    }
  }
}
