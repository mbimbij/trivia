import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {Identifiable} from "../../shared/identifiable";
import {AsyncPipe, NgIf} from "@angular/common";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-goto-game-button',
  standalone: true,
  imports: [
    RouterLink,
    AsyncPipe,
    NgIf,
    MatButton
  ],
  template: `
    <!--     TODO delete state, as the backend is called-->
    <button
      mat-stroked-button
      class="rounded"
      [attr.data-testid]="'goto-button-'+gameId"
      [disabled]="!canGoto"
      (click)="router.navigate(['/games',gameId])"
    >
      go to
    </button>
    {{ checkRender() }}
  `,
  styleUrl: './goto-game-button.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GotoGameButtonComponent extends Identifiable {
  @Input() gameId!: number
  @Input() userId!: string
  @Input() canGoto!: boolean;

  constructor(protected router: Router) {
    super()
  }
}
