export class CompanyModel {

  id: number;
  name: string;
  INN: string;

  constructor(id: number, name: string, INN: string) {
    this.id = id;
    this.name = name;
    this.INN = INN;
  }
}
