import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WaitingForEmailVerificationComponent } from './waiting-for-email-verification.component';
import {ActivatedRoute} from "@angular/router";
import {MockActivatedRoute} from "../../common/test-helpers";
import {
  AuthenticationServiceAbstract,
  AuthenticationServiceMock
} from "../../services/authentication-service.abstract";

describe('WaitingForEmailVerificationComponent', () => {
  let component: WaitingForEmailVerificationComponent;
  let fixture: ComponentFixture<WaitingForEmailVerificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WaitingForEmailVerificationComponent],
      providers:[
        {provide: AuthenticationServiceAbstract, useClass: AuthenticationServiceMock}
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WaitingForEmailVerificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
