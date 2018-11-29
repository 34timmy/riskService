import {RouterModule, Routes} from '@angular/router';
import {ModuleWithProviders} from '@angular/core';
import {RegisterComponent} from "./component/auth/register.component";
import {AuthComponent} from "./component/auth/auth.component";
import {AuthActivateGuard} from "./shared/auth.activate.guard";
import {ProfileComponent} from "./component/user/profile.component";
import {TableNamesComponent} from "./component/results_view/table-names.component";
import {ModelsListComponent} from "./component/constructor_diagram/models_list/models-list.component";
import {CompanyPicklistComponent} from "./component/company_picklist/company-picklist.component";
import {CompanyDataComponent} from "./component/company_picklist/company_data/company-data.component";


const appRoutes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: '/login',
  },
  {
    path: "login",
    component: AuthComponent,
  },
  {
    path: "register",
    component: RegisterComponent
  },
  {
    path: "profile",
    component: ProfileComponent,
    canActivate: [AuthActivateGuard]
  },
  {
    path: "results",
    component: TableNamesComponent,
    canActivate: [AuthActivateGuard]
  },
  {
    path: "constructor",
    component: ModelsListComponent,
    canActivate: [AuthActivateGuard]
  },
  {
    path: "calculation",
    component: CompanyPicklistComponent,
    canActivate: [AuthActivateGuard]
  },
  {
    path: 'data',
    component: CompanyDataComponent,
    canActivate: [AuthActivateGuard]
  }
];

export const routing: ModuleWithProviders = RouterModule
  .forRoot(appRoutes, {
    useHash: true
    // , onSameUrlNavigation: 'reload'
  });
