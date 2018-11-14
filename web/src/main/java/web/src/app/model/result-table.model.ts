export class ResultTableModel {

  id: number;
  descr: string;
  modelId: string;
  companyListId: string;
  companyListName: string;
  allCompaniesListId: string;
  year: string;
  date: string;

  constructor(modelId: string,
              companyListId: string, companyListName: string, allcompaniesListid: string, year: string, date: string) {
    this.modelId = modelId;
    this.companyListId = companyListId;
    this.companyListName = companyListName;
    this.allCompaniesListId = allcompaniesListid;
    this.year = year;
    this.date = date;
  }
}
