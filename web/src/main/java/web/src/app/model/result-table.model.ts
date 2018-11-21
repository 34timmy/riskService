export class ResultTableModel {

  id: number;
  descr: string;
  modelId: string;
  companyListId: string;
  companyListName: string;
  allCompaniesListId: string;
  year: string;
  tableName: string;
  date: string;

  constructor(modelId: string,
              companyListId: string, companyListName: string, allcompaniesListid: string, year: string, tableName: string) {
    this.modelId = modelId;
    this.companyListId = companyListId;
    this.companyListName = companyListName;
    this.allCompaniesListId = allcompaniesListid;
    this.year = year;
    this.tableName = tableName;
    let tableNameArr=tableName.toString().split("_");
    this.date = new Date(tableNameArr[tableNameArr.length - 1]).toDateString();
  }
}
