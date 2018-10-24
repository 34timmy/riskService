import {BrowserModule} from '@angular/platform-browser';
import {ErrorHandler, NgModule} from '@angular/core';
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
import {CodeHighlighterModule, DataListModule, SharedModule, TabViewModule} from 'primeng/primeng';
import {GrowlModule} from 'primeng/components/growl/growl';
import {FooterComponent} from './component/auth/footer.component';
import {TabMenuModule} from 'primeng/tabmenu';
import {TabViewComponent} from './component/tabmenu2/tab-view.component';
import {ToastModule} from 'primeng/toast';
import {ParamsEditComponent} from './component/params-picklist/params-edit.component';
import {ParamsService} from './service/params.service';
import {PickListModule} from 'primeng/picklist';
import {CompanyPicklistComponent} from './component/company-picklist/company-picklist.component';
import {ParamsPicklistComponent} from './component/params-picklist/params-picklist.component';
import {DropdownModule} from 'primeng/dropdown';
import {TreeTableModule} from 'primeng/treetable';
import {TreeViewComponent} from './component/treeview/treeview.components';
import {TreeService} from './service/tree.service';
import {FormulaEditComponent} from './component/formula/formula-edit.component';
import {ButtonModule} from 'primeng/button';
import {DialogModule} from 'primeng/dialog';
import {DragDropModule} from 'primeng/dragdrop';
import {Tree} from "./component/treediagram/tree.component";
import {Node} from "./component/treediagram/node";
import {NodesListService} from "./component/treediagram/services/nodesList.service";
import {TreeDiagramService} from "./service/tree-diagram.service";
import {ConfirmDialogModule} from 'primeng/confirmdialog';
import {ConfirmationService} from 'primeng/api';
import {ModelEditComponent} from "./component/model/model-edit.component";
import {NotificationService} from "./shared/notification.service";
import {CustomErrorHandler} from "./shared/custom.errorhandler";
import {RuleEditComponent} from "./component/rule/rule-edit.component";

@NgModule({
  declarations: [
    AppComponent,
    CompanyEditComponent,
    HeaderComponent, FooterComponent, TabViewComponent,
    ParamsEditComponent, CompanyPicklistComponent,
    ParamsPicklistComponent,
    TreeViewComponent,
    FormulaEditComponent,
    Tree,
    Node,
    ModelEditComponent,
    RuleEditComponent

  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpModule, FormsModule,
    ReactiveFormsModule, HttpModule,
    routing, CommonModule, DataTableModule,
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
    ConfirmDialogModule
  ],
  providers: [CompanyService, ParamsService, TreeService, NodesListService, TreeDiagramService,
    ConfirmationService, NotificationService, {provide: ErrorHandler, useClass: CustomErrorHandler}],
  bootstrap: [AppComponent],
  exports: [SharedModule, Tree,
    Node]
})
export class AppModule {
}

