import {ComponentFixture, TestBed} from '@angular/core/testing';

import {ValidationButtonComponent} from './validation-button.component';
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {GameService} from "../../game.service";
import {HttpClientTestingModule} from "@angular/common/http/testing";

describe('ValidationButtonComponent', () => {
  let component: ValidationButtonComponent;
  let fixture: ComponentFixture<ValidationButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ValidationButtonComponent, HttpClientTestingModule],
      providers: [
        {provide: GameServiceAbstract, useClass: GameService},
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ValidationButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
