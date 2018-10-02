import {Http} from '@angular/http';
import {Injectable} from '@angular/core';
import {I18Enum} from '../model/i18n.enum';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {basePath, i18nPath} from '../shared/config';
import {catchError} from 'rxjs/operators';


@Injectable()
export class I18nService {

  private cachedMessages: { string: string } = null;

  activeLocale: I18Enum = null;

  constructor(private http: Http) {

  }

  getMessage(key: string) {
    if (this.cachedMessages) {
      return this.cachedMessages[key];
    }
  }

  reloadLocale(locale: I18Enum) {
    this.activeLocale = locale;
    this.http.get(basePath + i18nPath + '/' + I18Enum[locale]).subscribe(
      res => {
        this.cachedMessages = res.json();
      }
    );
  }

  getCurrentLocale() {
    return I18Enum[this.activeLocale];
  }

  initMessages(locale: I18Enum) {
    this.activeLocale = locale;

    return new Promise((resolve, reject) => {
      this.http.get(basePath + i18nPath + '/' + I18Enum[locale])
        .pipe(map(res => res.json())).pipe(catchError((error: any): any => {
        reject(false);
        return Observable.throw(error.json().error || 'Server error');
      })).subscribe((callResult: { string: string }) => {
        this.cachedMessages = callResult;
        resolve(true);
      });

    });
  }
}
