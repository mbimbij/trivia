import {PlayerDto} from "../openapi-generated";

export class Player {
  id: string;
  name: string;
  coinCount: number;
  isInPenaltyBox: boolean;
  consecutiveIncorrectAnswersCount: number;

  constructor(id: string, name: string, coinCount: number, isInPenaltyBox: boolean, consecutiveIncorrectAnswersCount: number) {
    this.id = id;
    this.name = name;
    this.coinCount = coinCount;
    this.isInPenaltyBox = isInPenaltyBox;
    this.consecutiveIncorrectAnswersCount = consecutiveIncorrectAnswersCount;
  }

  static fromDto(dto: PlayerDto): Player {
    return new Player(dto.id,
      dto.name,
      dto.coinCount,
      dto.isInPenaltyBox,
      dto.consecutiveIncorrectAnswersCount)
  }

  toDto(): PlayerDto {
    return {
      id: this.id,
      name: this.name,
      coinCount: this.coinCount,
      isInPenaltyBox: this.isInPenaltyBox,
      consecutiveIncorrectAnswersCount: this.consecutiveIncorrectAnswersCount
    };
  }
}
