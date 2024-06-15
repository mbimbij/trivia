import {TestBed} from '@angular/core/testing';

import {GameService} from './game.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {mockGame1, mockGame2} from "../common/test-helpers";
import {Observable, of} from "rxjs";
import {Game} from "./game";
import {GameResponseDto, TriviaControllerService} from "../openapi-generated";

describe('GameService', () => {
  let defaultGameListReturnValues = [mockGame1, mockGame2].map(value => value.toDto())
  let defaultSingleGameReturnValue = mockGame1.toDto()

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
      expect(games).toEqual([mockGame1, mockGame2])
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
    expect(emittedValues).toEqual([mockGame1])

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
    let triviaControllerService: TriviaControllerService = TestBed.inject(TriviaControllerService);
    spyOn(triviaControllerService, `listGames`).and.returnValue(of(gameListReturnValues) as Observable<any>);
    spyOn(triviaControllerService, `getGameById`).and.returnValue(of(singleGameReturnValue) as Observable<any>);

    let service = TestBed.inject(GameService);
    spyOn(service, `registerGameCreatedHandler`);
    spyOn(service, `registerGameDeletedHandler`);
    spyOn(service, `registerGameUpdatedHandler`);

    return service;
  }
});
