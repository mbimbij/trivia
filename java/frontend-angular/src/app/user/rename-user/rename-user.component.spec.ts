import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RenameUserComponent } from './rename-user.component';

describe('RenameUserComponent', () => {
  let component: RenameUserComponent;
  let fixture: ComponentFixture<RenameUserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RenameUserComponent]
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
