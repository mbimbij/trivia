import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GameListComponent} from './game-list.component';
import {GameServiceAbstract} from "../game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {AppComponent} from "../app.component";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {CreateGameComponent} from "../create-game/create-game.component";
import {FormsModule} from "@angular/forms";
import {provideRouter, RouterModule} from "@angular/router";
import {RouterTestingModule} from "@angular/router/testing";

describe('GameListComponent', () => {
  let component: GameListComponent;
  let fixture: ComponentFixture<GameListComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
        {provide: GameServiceAbstract, useClass: GameServiceMock},
        provideRouter([])
      ],
      imports: [HttpClientTestingModule, CreateGameComponent, FormsModule],
    })
      .compileComponents();

    fixture = TestBed.createComponent(GameListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
