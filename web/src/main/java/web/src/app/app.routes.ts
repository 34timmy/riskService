import {RouterModule, Routes} from '@angular/router';
import {ModuleWithProviders} from '@angular/core';
import {TabViewComponent} from './component/tabmenu2/tab-view.component';
import {RegisterComponent} from "./component/auth/register.component";
import {AuthComponent} from "./component/auth/auth.component";
import {AuthActivateGuard} from "./shared/auth.activate.guard";
import {ProfileComponent} from "./component/user/profile.component";


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
    path: 'main',
    component: TabViewComponent,
    canActivate: [AuthActivateGuard]
  },
  {
    path: "profile",
    component: ProfileComponent,
    canActivate: [AuthActivateGuard]
  },
];

export const routing: ModuleWithProviders = RouterModule
  .forRoot(appRoutes, {
    useHash: true
    , onSameUrlNavigation: 'reload'
  });
