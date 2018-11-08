export class CompanyModel {

  id: number;
  descr: string;
  INN: string;

  constructor(id: number, name: string, INN: string) {
    this.id = id;
    this.descr = name;
    this.INN = INN;
  }
}
