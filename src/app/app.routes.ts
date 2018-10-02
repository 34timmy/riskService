import {Routes, RouterModule} from '@angular/router';
import {CompanyListComponent} from './component/company/company-list.component';
import {ModuleWithProviders} from '@angular/core';


const appRoutes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: '/app-company-list',
    },
      {
        path: 'app-company-list',
        component: CompanyListComponent
    }
];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes, {useHash: true});
