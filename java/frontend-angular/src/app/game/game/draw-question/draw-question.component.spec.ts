import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrawQuestionComponent } from './draw-question.component';
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {GameService} from "../../game.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('DrawQuestionComponent', () => {
  let component: DrawQuestionComponent;
  let fixture: ComponentFixture<DrawQuestionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DrawQuestionComponent, HttpClientTestingModule],
      providers: [
        {provide: GameServiceAbstract, useClass: GameService},
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrawQuestionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
