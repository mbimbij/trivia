import { TestBed } from '@angular/core/testing';
import { ResolveFn } from '@angular/router';

import { gameListResolver } from './game-list.resolver';

describe('gameListResolver', () => {
  const executeResolver: ResolveFn<any> = (...resolverParameters) =>
      TestBed.runInInjectionContext(() => gameListResolver(...resolverParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeResolver).toBeTruthy();
  });
});
