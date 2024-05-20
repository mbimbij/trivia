import {Component, Input} from '@angular/core';
import {GameResponseDto} from "../openapi-generated";
import {Router, RouterLink} from "@angular/router";

@Component({
  selector: 'app-goto-game-button',
  standalone: true,
  imports: [
    RouterLink
  ],
  template: `
    <button [disabled]="!isGameStarted()" (click)="router.navigate(['/games',game.id], { state: game })">
      go to
    </button>
  `,
  styleUrl: './goto-game-button.component.css'
})
export class GotoGameButtonComponent {
  @Input() game!: GameResponseDto

  constructor(protected router: Router) {
  }

  isGameStarted() {
    return this.game.state === 'started';
  }
}
