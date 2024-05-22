export class Player {
  id: string;
  name: string;
  coinCount: number;


  constructor(id: string, name: string, coinCount: number) {
    this.id = id;
    this.name = name;
    this.coinCount = coinCount;
  }
}
