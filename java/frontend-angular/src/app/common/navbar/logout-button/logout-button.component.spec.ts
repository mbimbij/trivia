import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LogoutButtonComponent } from './logout-button.component';
import {
  AuthenticationServiceAbstract,
  AuthenticationServiceMock
} from "../../../services/authentication-service.mock";

describe('LogoutButtonComponent', () => {
  let component: LogoutButtonComponent;
  let fixture: ComponentFixture<LogoutButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LogoutButtonComponent],
      providers:[
        {provide: AuthenticationServiceAbstract, useClass: AuthenticationServiceMock},
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LogoutButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
