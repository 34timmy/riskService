import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CompanyPicklistComponent} from './company-picklist.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      {path: '', component: CompanyPicklistComponent}
    ])
  ],
  exports: [
    RouterModule
  ]
})
export class CompanyPicklistRoutingModule {
}
