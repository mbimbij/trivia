export class User {
  id: string;
  name: string;
  isAnonymous: boolean

  constructor(id: string, name: string, isAnonymous: boolean) {
    this.id = id;
    this.name = name;
    this.isAnonymous = isAnonymous;
  }
}
