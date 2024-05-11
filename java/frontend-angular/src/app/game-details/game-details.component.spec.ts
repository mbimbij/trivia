import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GameDetailsComponent} from './game-details.component';
import {ActivatedRoute} from "@angular/router";
import {GameServiceAbstract} from "../game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {mockGame1} from "../test-helpers";
import {MockActivatedRoute} from "../mock-activated.route";
import {LocalStorageService, LocalStorageServiceTest} from "../local-storage.service";

describe('GameDetailsComponent', () => {
  let component: GameDetailsComponent;
  let fixture: ComponentFixture<GameDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        {provide: GameServiceAbstract, useClass: GameServiceMock},
        {provide: ActivatedRoute, useClass: MockActivatedRoute},
        {provide: LocalStorageService, useClass: LocalStorageServiceTest},
      ],
      imports: [GameDetailsComponent]
    })
      .compileComponents();

    Object.defineProperty(window, 'history', {
      value: {
        state: mockGame1
      }
    })

    fixture = TestBed.createComponent(GameDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});


