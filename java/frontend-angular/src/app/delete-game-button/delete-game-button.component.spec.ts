import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeleteGameButtonComponent } from './delete-game-button.component';

describe('DeleteGameButtonComponent', () => {
  let component: DeleteGameButtonComponent;
  let fixture: ComponentFixture<DeleteGameButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DeleteGameButtonComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DeleteGameButtonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
