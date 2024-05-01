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
    <button>
      <a [routerLink]="['/game',game.id]" [state]="game">
        go to
      </a>
    </button>
  `,
  styleUrl: './goto-game-button.component.css'
})
export class GotoGameButtonComponent {
  @Input() game!: GameResponseDto
}
