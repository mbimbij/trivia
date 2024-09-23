import {Component, Input} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatDialog} from "@angular/material/dialog";
import {DialogContentComponent} from "./dialog-content/dialog-content.component";
import {MatLabel} from "@angular/material/form-field";
import {User} from "../../user/user";

@Component({
  selector: 'app-create-game',
  standalone: true,
  imports: [
    MatButton,
    MatLabel
  ],
  template: `
    <button
      [attr.data-testid]="'create-game'"
      class="rounded"
      mat-raised-button color="primary" (click)="openDialog()">create game</button>
  `,
  styleUrl: './create-game.component.css'
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
}
