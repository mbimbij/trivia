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

export class Nobody extends User {
  private static _instance: Nobody = new Nobody();

  constructor() {
    super("id-Nobody", "Nobody", true);
  }

  static get instance(): Nobody {
    return this._instance;
  }
}
