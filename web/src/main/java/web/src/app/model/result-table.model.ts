export class ResultTableModel {

  id: number;
  modelName: string;
  modelId: string;
  companyListId: string;
  companyListName: string;
  allCompaniesListId: string;
  allCompaniesListName: string;
  year: string;
  tableName: string;
  date: string;
  userStarted: string;

  constructor(modelId: string, modelName: string,
              companyListId: string, companyListName: string, allcompaniesListid: string, allCompaniesListName: string, year: string, tableName: string, userStarted: string) {
    this.modelId = modelId;
    this.modelName = modelName;
    this.companyListId = companyListId;
    this.companyListName = companyListName;
    this.allCompaniesListId = allcompaniesListid;
    this.allCompaniesListName = allCompaniesListName;
    this.year = year;
    this.tableName = tableName;
    let tableNameArr = tableName.toString().split("_");
    this.date = this.convertDate(tableNameArr[tableNameArr.length - 1])
      .toLocaleString('ru');
    this.userStarted = userStarted;
  }

  convertDate(dateString) {
    var year = dateString.substring(0, 4);
    var month = dateString.substring(4, 6);
    var day = dateString.substring(6, 8);
    var hours = dateString.substring(9, 11);
    var minutes = dateString.substring(11, 13);
    var seconds = dateString.substring(13, 15);
    var ms = dateString.substring(15, 18);
    return new Date(year, month - 1, day, hours, minutes, seconds, ms);

  }
}
