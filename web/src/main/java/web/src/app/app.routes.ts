import {Routes, RouterModule} from '@angular/router';
import {ModuleWithProviders} from '@angular/core';
import {TabViewComponent} from './component/tabmenu2/tab-view.component';
import {ParamsPicklistComponent} from './component/params-picklist/params-picklist.component';
import {CompanyPicklistComponent} from './component/company-picklist/company-picklist.component';
import {TreeViewComponent} from "./component/treeview/treeview.components";


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
    path: 'params',
    component: ParamsPicklistComponent
  },
  {
    path: 'company',
    component: CompanyPicklistComponent
  },
  {
    path: 'constructor',
    component: TreeViewComponent,
    // canActivate: [AuthenticationGuard],
    runGuardsAndResolvers: 'always',
  }
];

export const routing: ModuleWithProviders = RouterModule
  .forRoot(appRoutes, {
    useHash: true,
    onSameUrlNavigation: 'reload'
  });
