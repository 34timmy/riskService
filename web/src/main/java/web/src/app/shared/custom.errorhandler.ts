import { ErrorHandler, Inject } from '@angular/core';
import { NotificationService } from './notification.service';


export class CustomErrorHandler implements ErrorHandler {

  constructor(@Inject(NotificationService) private notificationService: NotificationService) {
  }

  handleError(error: any): void {
    this.showErrorInConsole(error);

      this.notificationService.error(error.json().Message);
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
