import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {ParamsPicklistComponent} from './params-picklist.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      {path: '', component: ParamsPicklistComponent}
    ])
  ],
  exports: [
    RouterModule
  ]
})
export class ParamsPicklistRoutingModule {
}
