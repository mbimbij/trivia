export class Game{
  "id": number
  "name": string
  "state": string;

  constructor(id: number, name: string, state: string) {
    this.id = id;
    this.name = name;
    this.state = state;
  }
}
