import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DeleteGameButtonComponent} from './delete-game-button.component';
import {GameServiceAbstract} from "../game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {UserServiceAbstract} from "../../user/user-service.abstract";
import {UserServiceTest} from "../../adapters/user/user-service.test";
import {mockGame1} from "../../common/test-helpers";

describe('DeleteGameButtonComponent', () => {
  let component: DeleteGameButtonComponent;
  let fixture: ComponentFixture<DeleteGameButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteGameButtonComponent],
      providers: [
        {provide: GameServiceAbstract, useClass: GameServiceMock},
        {provide: UserServiceAbstract, useClass: UserServiceTest}
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(DeleteGameButtonComponent);
    component = fixture.componentInstance;
    component.game = mockGame1
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
