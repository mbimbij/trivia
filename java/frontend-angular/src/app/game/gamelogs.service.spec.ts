import {TestBed} from '@angular/core/testing';

import {GameService} from './game.service';
import {HttpClientTestingModule} from '@angular/common/http/testing';
import {GameLogsControllerService} from "../openapi-generated/gamelogs";
import {GameLogsService} from "./gamelogs.service";

describe('GameService', () => {
  it('should be created', () => {
    let service = createService()
    expect(service).toBeTruthy();
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

  function createService() {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    let triviaControllerService: GameLogsControllerService = TestBed.inject(GameLogsControllerService);

    return TestBed.inject(GameLogsService);
  }
});
