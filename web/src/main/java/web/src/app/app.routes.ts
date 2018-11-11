import {RouterModule, Routes} from '@angular/router';
import {ModuleWithProviders} from '@angular/core';
import {TabViewComponent} from './component/tabmenu2/tab-view.component';
import {CompanyPicklistComponent} from './component/company-picklist/company-picklist.component';
import {TreeViewComponent} from "./component/results_view/treeview.components";


const appRoutes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: '/app-tabpanel',
  },
  {
    path: 'app-tabpanel',
    component: TabViewComponent
  },
  {
    path: 'company',
    component: CompanyPicklistComponent
  },
  {
    path: 'results',
    component: TreeViewComponent
  }
];

export const routing: ModuleWithProviders = RouterModule
  .forRoot(appRoutes, {
    useHash: true,
    onSameUrlNavigation: 'reload'
  });
