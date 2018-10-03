import {Routes, RouterModule} from '@angular/router';
import {ModuleWithProviders} from '@angular/core';
import {TabViewComponent} from './component/tabmenu2/tab-view.component';


const appRoutes: Routes = [
    {
        path: '',
        pathMatch: 'full',
        redirectTo: '/app-tabpanel',
    },
      {
        path: 'app-tabpanel',
        component: TabViewComponent
    }
];

export const routing: ModuleWithProviders = RouterModule.forRoot(appRoutes, {useHash: true});
