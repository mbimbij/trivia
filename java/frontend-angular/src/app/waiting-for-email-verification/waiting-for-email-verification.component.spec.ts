import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WaitingForEmailVerificationComponent } from './waiting-for-email-verification.component';

describe('WaitingForEmailVerificationComponent', () => {
  let component: WaitingForEmailVerificationComponent;
  let fixture: ComponentFixture<WaitingForEmailVerificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WaitingForEmailVerificationComponent]
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
