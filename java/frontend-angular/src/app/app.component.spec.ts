import {TestBed} from '@angular/core/testing';
import {AppComponent} from './app.component';
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {GameServiceMock} from "./game-service-mock";
import {GameServiceAbstract} from "./game-service-abstract";
import {CreateGameComponent} from "./create-game/create-game.component";
import {FormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";

describe('AppComponent', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [{provide: GameServiceAbstract, useClass: GameServiceMock}],
    declarations: [AppComponent],
    imports: [ HttpClientTestingModule, CreateGameComponent, FormsModule, RouterModule ]
  }));

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it(`should have as title 'frontend-angular'`, () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('frontend-angular');
  });

  it('should render title', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('p')?.textContent).toContain('Welcome to Trivia Web !');
  });
});
