import {generateRandomString} from "./helpers";

export class Identifiable{
    protected readonly id: string;

    constructor() {
        this.id = `${this.constructor.name} - ${generateRandomString(4)}`;
    }

    checkRender(){
      let message = `${this.id} - view refresh`;
      console.log(message)
      return ''
    }
}
