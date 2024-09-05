import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RenameUserComponent } from './rename-user.component';
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {UserServiceMock} from "../../adapters/user/user-service.mock";
import {AuthenticationServiceAbstract, AuthenticationServiceMock} from "../../services/authentication-service-abstract";

describe('RenameUserComponent', () => {
  let component: RenameUserComponent;
  let fixture: ComponentFixture<RenameUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RenameUserComponent],
      providers:[
        {provide: UserServiceAbstract, useClass: UserServiceMock},
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RenameUserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
