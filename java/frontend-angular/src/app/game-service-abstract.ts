import {Observable} from "rxjs";
import {GameResponseDto} from "./openapi-generated";

export abstract class GameServiceAbstract {
  abstract getGames(): Observable<Array<GameResponseDto>>;
}

