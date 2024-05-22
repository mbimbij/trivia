import {ComponentFixture, TestBed} from '@angular/core/testing';

import {DeleteGameButtonComponent} from './delete-game-button.component';
import {GameServiceAbstract} from "../game-service-abstract";
import {GameServiceMock} from "../game-service-mock";
import {UserService, LocalStorageServiceTest} from "../user.service";
import {mockGame1} from "../test-helpers";

describe('DeleteGameButtonComponent', () => {
  let component: DeleteGameButtonComponent;
  let fixture: ComponentFixture<DeleteGameButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteGameButtonComponent],
      providers: [
        {provide: GameServiceAbstract, useClass: GameServiceMock},
        {provide: UserService, useClass: LocalStorageServiceTest}
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
