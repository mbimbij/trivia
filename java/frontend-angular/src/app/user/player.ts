import {State} from "../openapi-generated/game";

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

  public isWaitingToValidateRollDiceInsidePenaltyBox() {
    return new Set<State>([
      State.WaitingToValidateEvenDiceRollFromPenaltyBox,
      State.WaitingToValidateOddDiceRollFromPenaltyBox
    ]).has(this.state);
  }

  public isWaitingToValidateRollDiceOutsidePenaltyBox() {
    return new Set<State>([
      State.WaitingToDraw1StQuestion
    ]).has(this.state);
  }

  public isWaitingToValidateCorrectAnswer() {
    return new Set<State>([
      State.WaitingToValidateFirstCorrectAnswer,
      State.WaitingToValidateSecondCorrectAnswer
    ]).has(this.state);
  }

  public isWaitingToValidateFirstIncorrectAnswer() {
    return new Set<State>([
      State.WaitingToValidateFirstIncorrectAnswer
    ]).has(this.state);
  }

  public isWaitingToValidateSecondIncorrectAnswer() {
    return new Set<State>([
      State.WaitingToValidateSecondIncorrectAnswer
    ]).has(this.state);
  }
}
