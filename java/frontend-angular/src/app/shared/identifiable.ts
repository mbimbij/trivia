import {generateRandomString} from "./helpers";

export class Identifiable {
  private readonly _id: string;

  constructor() {
    this._id = `${this.constructor.name} - ${generateRandomString(4)}`;
  }

  get id(): string {
    return this._id;
  }

  checkRender() {
    let message = `${this.id} - view refresh`;
    console.log(message)
    return ''
  }
}
