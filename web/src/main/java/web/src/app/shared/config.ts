import {RequestOptions, Headers} from '@angular/http';

export const basePath = '';
export const constructorPath = '/constructor';
export const modelPath = '/model';
export const formulaPath = '/formulas';
export const profilePath = '/profile';
export const registerPath = '/register';
export const companiesPath = '/admin/users';
export const restaurantPath = '/profile/restaurants';
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
