import {TestBed} from '@angular/core/testing';

import {GameService} from './game.service';
import {HttpClientTestingModule, HttpTestingController} from '@angular/common/http/testing';
import {mockGame1, mockGame2} from "../common/test-helpers";
import {BehaviorSubject, Observable, of, throttleTime} from "rxjs";
import {TestScheduler} from 'rxjs/testing';
import {Game} from "./game";
import {GameResponseDto, TriviaControllerService} from "../openapi-generated";
import {HttpEvent} from "@angular/common/http";

describe('GameService', () => {
  let service: GameService;
  let triviaControllerService: TriviaControllerService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule]
    });
    triviaControllerService = TestBed.inject(TriviaControllerService);
    service = TestBed.inject(GameService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  const testScheduler = new TestScheduler((actual, expected) => {
    // asserting the two objects are equal - required
    // for TestScheduler assertions to work via your test framework
    // e.g. using chai.
    expect(actual).toEqual(expected);
  });

  it('should emit the expected values', () => {
    testScheduler.run((helpers) => {
      const {cold, time, expectObservable, expectSubscriptions} = helpers;
      // const e1 = cold(' -a--b--c---|');
      const e1 = cold(' -a-b------cd----|');
      const e1subs = '  ^---------------!';
      // const t = time('  ---|       '); // t = 3
      const t = 3; // t = 3
      //             '   ---|     ---|---|---|---|   ')
      const expected = '-a--b-----c--d--|';

      expectObservable(e1.pipe(throttleTime(t, testScheduler, {leading: true, trailing: true}))).toBe(expected);
      expectSubscriptions(e1.subscriptions).toBe(e1subs);
    });
  });

  it('should return games when calling getGames()', () => {
    let observable = of([mockGame1, mockGame2].map(value => value.toDto()));
    spyOn(triviaControllerService, `listGames`).and.returnValue(
      observable as Observable<any>
    );

    // WHEN
    service.getGames().subscribe(games => {
      expect(games).toEqual([mockGame1, mockGame2])
    })
  });
});
