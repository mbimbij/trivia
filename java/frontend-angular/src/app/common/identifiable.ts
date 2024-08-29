import {generateRandomString} from "./helpers";

export class Identifiable{
    protected readonly id: string;

    constructor() {
        this.id = `${this.constructor.name} - ${generateRandomString(4)}`;
    }

    checkRender(){
      console.log(`${this.id} is rendering`)
    }
}
