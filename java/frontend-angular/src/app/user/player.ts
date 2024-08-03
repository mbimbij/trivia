import {PlayerDto, State} from "../openapi-generated";

export class Player {
  id: string;
  name: string;
  coinCount: number;
  isInPenaltyBox: boolean;
  consecutiveIncorrectAnswersCount: number;
  state: State;
  location: number;
  gotOutOfPenaltyBox: boolean;

  constructor(id: string,
              name: string,
              coinCount: number,
              isInPenaltyBox: boolean,
              consecutiveIncorrectAnswersCount: number,
              state: State,
              location: number,
              gotOutOfPenaltyBox: boolean
  ) {
    this.id = id;
    this.name = name;
    this.coinCount = coinCount;
    this.isInPenaltyBox = isInPenaltyBox;
    this.consecutiveIncorrectAnswersCount = consecutiveIncorrectAnswersCount;
    this.state = state;
    this.location = location;
    this.gotOutOfPenaltyBox = gotOutOfPenaltyBox;
  }

  static fromDto(dto: PlayerDto): Player {
    return new Player(dto.id,
      dto.name,
      dto.coinCount,
      dto.isInPenaltyBox,
      dto.consecutiveIncorrectAnswersCount,
      dto.state,
      dto.location,
      dto.gotOutOfPenaltyBox
    )
  }

  toDto(): PlayerDto {
    return {
      id: this.id,
      name: this.name,
      coinCount: this.coinCount,
      isInPenaltyBox: this.isInPenaltyBox,
      consecutiveIncorrectAnswersCount: this.consecutiveIncorrectAnswersCount,
      state: this.state,
      location: this.location,
      gotOutOfPenaltyBox: this.gotOutOfPenaltyBox
    };
  }
}
