export class CompanyDataModel {
  descr: string;
  paramCode: string;
  year: number;
  paramValue: string;


  constructor(paramCode: string, descr: string, year: number, paramValue: string) {
    this.descr = descr;
    this.paramCode = paramCode;
    this.year = year;
    this.paramValue = paramValue;
  }
}
