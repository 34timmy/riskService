import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {I18nService} from '../../service/i18n.service';
import {I18Enum} from '../../model/i18n.enum';

@Component({
  templateUrl: './header.html',
  selector: 'app-header-component'
})
export class HeaderComponent {


  constructor(
    private router: Router,
    private formBuilder: FormBuilder) {
  }

  chooseEng() {
    // this.i18Service.reloadLocale(I18Enum.en);
  }

  chooseRu() {
    // this.i18Service.reloadLocale(I18Enum.ru);
  }
}
