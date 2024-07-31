import {ComponentFixture, TestBed} from '@angular/core/testing';

import {GotoGameButtonComponent} from './goto-game-button.component';
import {ActivatedRoute} from "@angular/router";
import {getMockGame1, MockActivatedRoute} from "../../common/test-helpers";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {UserServiceMock} from "../../adapters/user/user-service.mock";

describe('GotoGameButtonComponent', () => {
  let component: GotoGameButtonComponent;
  let fixture: ComponentFixture<GotoGameButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GotoGameButtonComponent],
      providers: [
        {provide: ActivatedRoute, useClass: MockActivatedRoute},
        {provide: UserServiceAbstract, useClass: UserServiceMock},
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GotoGameButtonComponent);
    component = fixture.componentInstance;
    component.game = getMockGame1()
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
