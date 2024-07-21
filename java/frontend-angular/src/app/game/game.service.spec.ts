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

  const testCases = [
    {log: "message", expectedLogsCount: 1},
    {log: "message1\nmessage2", expectedLogsCount: 2},
    {log: "message1\nmessage2\nmessage3", expectedLogsCount: 3},
    {log: "message1\n", expectedLogsCount: 1},
    {log: "\nmessage1\n\n", expectedLogsCount: 1},
    {log: "message1\n\nmessage2", expectedLogsCount: 2},
    {log: "message1\n\n\n\nmessage2\n", expectedLogsCount: 2},
  ]

  testCases.forEach(
    (
      {log, expectedLogsCount},
      tcNum
    ) => {
      it(`GIVEN case number ${tcNum}, then expected result should be ${expectedLogsCount}`,
        () => {
          // GIVEN
          let service = createService();
          let newGameLog = {gameId: 1, value: log};

          // WHEN
          service.handleNewGameLog(newGameLog);

          // THEN new game log is split into multiple lines
          service.getGameLogs(1).subscribe(logs => {
            expect(logs).toHaveSize(expectedLogsCount);
          })
        }
      );
    }
  )

  it('should add multiple game log lines if text contains line break', () => {
    // GIVEN
    let service = createService();
    let newGameLog = {gameId: 1, value: "message1\nmessage2"};

    // WHEN
    service.handleNewGameLog(newGameLog);

    // THEN new game log is split into multiple lines
    service.getGameLogs(1).subscribe(logs => {
      expect(logs).toHaveSize(2);
    })
  });

  it('should split initial game logs if text contains line break', () => {
    // GIVEN
    let service = createService();
    let gameLogs = [
      {gameId: 1, value: "message0"},
      {gameId: 1, value: "message1\nmessage2"},
      {gameId: 1, value: "\n\nmessage3\n\n\nmessage4\n"},
      {gameId: 1, value: "\n\n\n\n\n\n"},
      {gameId: 1, value: ""},
    ];

    // AND
    let expectedSplitLogs = [
      {gameId: 1, value: "message0"},
      {gameId: 1, value: "message1"},
      {gameId: 1, value: "message2"},
      {gameId: 1, value: "message3"},
      {gameId: 1, value: "message4"},
    ];

    // WHEN
    let splitLogs = service.splitLogs(gameLogs);

    // THEN new game log is split into multiple lines
    expect(splitLogs).toEqual(expectedSplitLogs);
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
