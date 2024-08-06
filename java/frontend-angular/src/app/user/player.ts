import {State} from "../openapi-generated";

export class Player {
  id: string;
  name: string;
  coinCount: number;
  state: State;
  location: number;
  gotOutOfPenaltyBox: boolean;

  constructor(id: string,
              name: string,
              coinCount: number,
              state: State,
              location: number,
              gotOutOfPenaltyBox: boolean
  ) {
    this.id = id;
    this.name = name;
    this.coinCount = coinCount;
    this.state = state;
    this.location = location;
    this.gotOutOfPenaltyBox = gotOutOfPenaltyBox;
  }
}
