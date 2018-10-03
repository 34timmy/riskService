import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AppComponent} from './app.component';
import {CommonModule} from '@angular/common';
import {HeaderComponent} from './component/auth/header.component';
import {CompanyEditComponent} from './component/company-picklist/company-edit.component';
import {CompanyService} from './service/company.service';
import {routing} from './app.routes';
import {DataTableModule} from 'primeng/components/datatable/datatable';
import {CodeHighlighterModule, DataListModule, TabViewModule} from 'primeng/primeng';
import {GrowlModule} from 'primeng/components/growl/growl';
import {FooterComponent} from './component/auth/footer.component';
import {TabMenuModule} from 'primeng/tabmenu';
import {TabViewRoutingModule} from './component/tabmenu2/tabmenu-routing.module';
import {TabViewComponent} from './component/tabmenu2/tab-view.component';
import {ToastModule} from 'primeng/toast';
import {ParamsEditComponent} from './component/params-picklist/params-edit.component';
import {ParamsService} from './service/params.service';
import {PickListModule} from 'primeng/picklist';
import {CompanyPicklistComponent} from './component/company-picklist/company-picklist.component';
import {CompanyPicklistRoutingModule} from './component/company-picklist/company-picklist-routing.module';
import {ParamsPicklistRoutingModule} from './component/params-picklist/params-picklist-routing.module';
import {ParamsPicklistComponent} from './component/params-picklist/params-picklist.component';
import {DropdownModule} from 'primeng/dropdown';

@NgModule({
  declarations: [
    AppComponent,
    CompanyEditComponent,
    HeaderComponent, FooterComponent, TabViewComponent,
    ParamsEditComponent, CompanyPicklistComponent,
    ParamsPicklistComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule, FormsModule,
    ReactiveFormsModule, HttpModule,
    routing, CommonModule, DataTableModule,
    DataListModule, GrowlModule, TabMenuModule, CommonModule,
    TabViewRoutingModule,
    ToastModule,
    TabViewModule,
    CodeHighlighterModule,
    PickListModule,
    CompanyPicklistRoutingModule,
    ParamsPicklistRoutingModule,
    DropdownModule,
    BrowserAnimationsModule

  ],
  providers: [CompanyService, ParamsService],
  bootstrap: [AppComponent]
})
export class AppModule {
}

