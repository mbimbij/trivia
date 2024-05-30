import {PlayerDto} from "../openapi-generated";

export class Player {
  id: string;
  name: string;
  coinCount: number;

  constructor(id: string, name: string, coinCount: number) {
    this.id = id;
    this.name = name;
    this.coinCount = coinCount;
  }

  static fromDto(dto: PlayerDto): Player {
    return new Player(dto.id, dto.name, dto.coinCount)
  }

  toDto(): PlayerDto {
    return {id: this.id, name: this.name, coinCount: this.coinCount};
  }
}
