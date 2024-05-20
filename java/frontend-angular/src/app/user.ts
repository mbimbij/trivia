export class User {
  idInteger: number;
  id: string;
  name: string;

  constructor(id: string, idInteger: number, name: string) {
    this.id = id;
    this.idInteger = idInteger;
    this.name = name;
  }
}
