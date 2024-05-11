import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GotoGameButtonComponent } from './goto-game-button.component';
import {ActivatedRoute} from "@angular/router";
import {MockActivatedRoute} from "../mock-activated.route";
import {mockGame1} from "../test-helpers";

describe('GotoGameButtonComponent', () => {
  let component: GotoGameButtonComponent;
  let fixture: ComponentFixture<GotoGameButtonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [GotoGameButtonComponent],
      providers: [
        {provide: ActivatedRoute, useClass: MockActivatedRoute}
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(GotoGameButtonComponent);
    component = fixture.componentInstance;
    component.game = mockGame1
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});