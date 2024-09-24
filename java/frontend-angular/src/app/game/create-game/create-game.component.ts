import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatDialog} from "@angular/material/dialog";
import {DialogContentComponent} from "./dialog-content/dialog-content.component";
import {MatLabel} from "@angular/material/form-field";
import {User} from "../../user/user";

export class CreateGameComponentTestIds{
  public static readonly OPEN_DIALOG_BUTTON = 'create-game'
  public static readonly DIALOG = 'create-game-dialog'
  public static readonly GAME_NAME = 'game-name'
  public static readonly CREATOR_NAME = 'creator-name'
  public static readonly CANCEL = 'cancel'
  public static readonly RESET = 'reset'
  public static readonly VALIDATE = 'validate'
}

@Component({
  selector: 'app-create-game',
  standalone: true,
  imports: [
    MatButton,
    MatLabel
  ],
  template: `
    <button
      [attr.data-testid]="CreateGameComponentTestIds.OPEN_DIALOG_BUTTON"
      class="rounded"
      mat-raised-button color="primary" (click)="openDialog()">create game
    </button>
  `,
  styleUrl: './create-game.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateGameComponent {
  @Input() user!: User
  readonly dialog!: MatDialog

  constructor(dialog: MatDialog) {
    this.dialog = dialog;
  }

  openDialog() {
    let dialogRef = this.dialog.open(DialogContentComponent);
    dialogRef.componentRef?.setInput('user', this.user)
  }

  protected readonly CreateGameComponentTestIds = CreateGameComponentTestIds;
}
