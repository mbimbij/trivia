import {TestBed} from '@angular/core/testing';

import {GameService} from './game.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {getMockGame1, getMockGame2} from "../shared/test-helpers";
import {Observable, of} from "rxjs";
import {Game} from "./game";
import {gameToGameDto} from "../shared/helpers";
import {GameControllerService, GameResponseDto} from "../openapi-generated/game";

describe('GameService', () => {
  let defaultGameListReturnValues = [getMockGame1(), getMockGame2()].map(value => gameToGameDto(value))
  let defaultSingleGameReturnValue = gameToGameDto(getMockGame1())

  it('should be created', () => {
    let service = createService()
    expect(service).toBeTruthy();
  });

  it('should return games when calling getGames()', () => {
    // GIVEN
    let service = createService()

    // WHEN
    let gamesObservable = service.getGames();

    // THEN
    gamesObservable.subscribe(games => {
      expect(games).toEqual([getMockGame1(), getMockGame2()])
    })

    expect(service.registerGameCreatedHandler).toHaveBeenCalledTimes(1)
    expect(service.registerGameDeletedHandler).toHaveBeenCalledTimes(1)
    expect(service.registerGameUpdatedHandler).toHaveBeenCalledTimes(2)
  });

  it('should return empty array when calling getGames() and no games returned', () => {
    // GIVEN
    let service = createService([])

    // WHEN
    let gamesObservable = service.getGames();

    // THEN
    gamesObservable.subscribe(games => {
      expect(games).toEqual([])
    })

    expect(service.registerGameCreatedHandler).toHaveBeenCalledTimes(1)
    expect(service.registerGameDeletedHandler).toHaveBeenCalledTimes(1)
    expect(service.registerGameUpdatedHandler).toHaveBeenCalledTimes(0)
  });

  it('should return single game', () => {
    // GIVEN
    let service = createService();
    let emittedValues: Game[] = [];

    // WHEN
    service.getGame(1).subscribe(game => {
      emittedValues.push(game)
    })

    // THEN game is returned
    expect(emittedValues).toEqual([getMockGame1()])

    // AND handler registration is performed as expected
    expect(service.registerGameCreatedHandler).toHaveBeenCalledTimes(0)
    expect(service.registerGameDeletedHandler).toHaveBeenCalledTimes(0)
    expect(service.registerGameUpdatedHandler).toHaveBeenCalledTimes(1)
  });


  function createService(gameListReturnValues: GameResponseDto[] = defaultGameListReturnValues,
                         singleGameReturnValue: GameResponseDto = defaultSingleGameReturnValue
  ) {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    let triviaControllerService: GameControllerService = TestBed.inject(GameControllerService);
    spyOn(triviaControllerService, `listGames`).and.returnValue(of(gameListReturnValues) as Observable<any>);
    spyOn(triviaControllerService, `getGameById`).and.returnValue(of(singleGameReturnValue) as Observable<any>);

    let service = TestBed.inject(GameService);
    spyOn(service, `registerGameCreatedHandler`);
    spyOn(service, `registerGameDeletedHandler`);
    spyOn(service, `registerGameUpdatedHandler`);

    return service;
  }
});
