export class CompanyModel {

  id: number;
  descr: string;
  inn: string;

  constructor(id: number, name: string, inn: string) {
    this.id = id;
    this.descr = name;
    this.inn = inn;
  }
}
