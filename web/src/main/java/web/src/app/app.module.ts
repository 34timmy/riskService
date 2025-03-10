import {BrowserModule} from '@angular/platform-browser';
import {ErrorHandler, NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {AppComponent} from './app.component';
import {CommonModule} from '@angular/common';
import {HeaderComponent} from './component/auth/header.component';
import {CompanyEditComponent} from './component/company_picklist/company-edit.component';
import {CompanyService} from './service/company.service';
import {routing} from './app.routes';
import {
  CalendarModule,
  CodeHighlighterModule, ContextMenu, ContextMenuModule,
  DataListModule,
  MessageService,
  ProgressBarModule,
  SharedModule,
  SidebarModule, SliderModule,
  TabViewModule
} from 'primeng/primeng';
import {GrowlModule} from 'primeng/components/growl/growl';
import {FooterComponent} from './component/auth/footer.component';
import {TabMenuModule} from 'primeng/tabmenu';
import {ToastModule} from 'primeng/toast';
import {PickListModule} from 'primeng/picklist';
import {CompanyPicklistComponent} from './component/company_picklist/company-picklist.component';
import {DropdownModule} from 'primeng/dropdown';
import {TreeTableModule} from 'primeng/treetable';
import {TreeViewComponent} from './component/results_view/treeview.components';
import {TreeService} from './service/tree.service';
import {FormulaEditComponent} from './component/constructor_diagram/formula/formula-edit.component';
import {ButtonModule} from 'primeng/button';
import {DialogModule} from 'primeng/dialog';
import {DragDropModule} from 'primeng/dragdrop';
import {Tree} from "./component/constructor_diagram/tree.component";
import {Node} from "./component/constructor_diagram/node";
import {NodesListService} from "./component/constructor_diagram/services/nodesList.service";
import {TreeDiagramService} from "./service/tree-diagram.service";
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {ConfirmationService} from 'primeng/api';
import {ModelEditComponent} from "./component/constructor_diagram/model/model-edit.component";
import {NotificationService} from "./shared/notification.service";
import {CustomErrorHandler} from "./shared/custom.errorhandler";
import {RuleEditComponent} from "./component/rule/rule-edit.component";
import {ModelcalcEditComponent} from "./component/constructor_diagram/modelcalc/modelcalc-edit.component";
import {CompanySaveComponent} from "./component/company_picklist/company-save.component";
import {AutoCompleteModule} from 'primeng/autocomplete';
import {CompanyListComponent} from "./component/company_picklist/company-list.component";
import {TableNamesComponent} from "./component/results_view/table-names.component";
import {GroupByPipe} from "./shared/groupBy.pipe";
import {TableModule} from "primeng/table";
import {FileUploadModule} from 'primeng/fileupload';
import {HttpClientModule} from "@angular/common/http";
import {UploadService} from "./service/upload.service";
import {FileSelectDirective} from 'ng2-file-upload';
import {ChooseComponent} from "./component/constructor_diagram/choose_dialog/choose.component";
import {UserService} from "./service/user.service";
import {AuthService} from "./service/auth.service";
import {ProfileComponent} from "./component/user/profile.component";
import {UserEditComponent} from "./component/user/user-edit.component";
import {UserListComponent} from "./component/user/user-list.component";
import {AuthComponent} from "./component/auth/auth.component";
import {RegisterComponent} from "./component/auth/register.component";
import {ProfileService} from "./service/profile.service";
import {ModelsListComponent} from "./component/constructor_diagram/models_list/models-list.component";
import {OverlayPanelModule} from 'primeng/overlaypanel';
import {InputTextareaModule} from 'primeng/inputtextarea';
import {AuthActivateGuard} from "./shared/auth.activate.guard";
import {MenuModule} from 'primeng/menu';
import {CheckboxModule} from 'primeng/checkbox';
import {CompanyDataComponent} from "./component/company_picklist/company_data/company-data.component";
import {ProgressSpinnerModule} from 'primeng/progressspinner';
import {DataTabComponent} from "./component/data_tab/data-tab.component";
import {ResultTableComponent} from "./component/results_view/result-table.component";

@NgModule({
  declarations: [
    AppComponent,
    CompanyEditComponent,
    HeaderComponent, FooterComponent,
    CompanyPicklistComponent,
    TreeViewComponent,
    FormulaEditComponent,
    Tree,
    Node,
    ModelEditComponent,
    RuleEditComponent,
    ModelcalcEditComponent,
    CompanyListComponent,
    CompanySaveComponent,
    TableNamesComponent,
    GroupByPipe,
    FileSelectDirective,
    ChooseComponent,
    ProfileComponent,
    UserEditComponent,
    UserListComponent,
    AuthComponent,
    HeaderComponent,
    RegisterComponent,
    ModelsListComponent,
    CompanyDataComponent,
    DataTabComponent,
    ResultTableComponent

  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule, FormsModule,
    ReactiveFormsModule, HttpModule,
    routing, CommonModule, TableModule,
    DataListModule, GrowlModule, TabMenuModule, CommonModule,
    ToastModule,
    TabViewModule,
    CodeHighlighterModule,
    PickListModule,
    DropdownModule,
    BrowserAnimationsModule,
    TreeTableModule,
    ButtonModule,
    DialogModule,
    DragDropModule,
    ConfirmDialogModule,
    AutoCompleteModule,
    FileUploadModule,
    HttpClientModule,
    SidebarModule,
    ProgressBarModule,
    OverlayPanelModule,
    InputTextareaModule,
    MenuModule,
    CheckboxModule,
    ProgressSpinnerModule,
    CalendarModule,
    SliderModule,
    ContextMenuModule
  ],
  providers: [CompanyService, UserService, TreeService,
    NodesListService, TreeDiagramService,
    ConfirmationService, NotificationService,
    UploadService,
    AuthService, ProfileService,
    AuthActivateGuard,
    MessageService,
    {provide: ErrorHandler, useClass: CustomErrorHandler}],
  bootstrap: [AppComponent],
  exports: [SharedModule, Tree,
    Node]
})
export class AppModule {
}

