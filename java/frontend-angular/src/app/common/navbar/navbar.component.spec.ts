import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavbarComponent } from './navbar.component';
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {UserServiceMock} from "../../adapters/user/user-service.mock";
import {AuthenticationServiceAbstract, AuthenticationServiceMock} from "../../services/authentication-service.abstract";

describe('NavbarComponent', () => {
  let component: NavbarComponent;
  let fixture: ComponentFixture<NavbarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavbarComponent],
      providers:[
        {provide: UserServiceAbstract, useClass: UserServiceMock},
        {provide: AuthenticationServiceAbstract, useClass: AuthenticationServiceMock},
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavbarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
