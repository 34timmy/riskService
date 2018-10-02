import {NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {TabViewComponent} from './tab-view.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      {path: '', component: TabViewComponent}
    ])
  ],
  exports: [
    RouterModule
  ]
})
export class TabViewRoutingModule {
}
