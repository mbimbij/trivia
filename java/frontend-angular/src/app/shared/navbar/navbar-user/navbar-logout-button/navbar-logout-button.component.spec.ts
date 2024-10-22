import {ComponentFixture, TestBed} from '@angular/core/testing';

import {NavbarLogoutButtonComponent} from './navbar-logout-button.component';
import {
  AuthenticationServiceAbstract,
  AuthenticationServiceMock
} from "../../../../services/authentication-service-abstract";

describe('LogoutButtonComponent', () => {
  let component: NavbarLogoutButtonComponent;
  let fixture: ComponentFixture<NavbarLogoutButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NavbarLogoutButtonComponent],
      providers:[
        {provide: AuthenticationServiceAbstract, useClass: AuthenticationServiceMock},
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavbarLogoutButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
