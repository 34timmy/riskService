import {RequestOptions, Headers} from '@angular/http';

export const basePath = '';
export const constructorPath = '/constructor';
export const modelPath = '/models';
export const modelCalcPath = '/modelCalcs';
export const rulePath = '/rules';
export const formulaPath = '/formulas';
export const companiesPath = '/companies';
export const companyLists = '/companyLists';
export const i18nPath = '/i18n';


export const headers: Headers = new Headers({
  'Content-Type': 'application/json'
});
export const reqOptions: RequestOptions = new RequestOptions({
  withCredentials: true
});
export const reqOptionsJson: RequestOptions = new RequestOptions({
  withCredentials: true,
  headers: headers
});
