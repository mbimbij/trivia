import {ChangeDetectionStrategy, Component, Input, SimpleChanges} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatDialog} from "@angular/material/dialog";
import {DialogContentComponent} from "./dialog-content/dialog-content.component";
import {MatLabel} from "@angular/material/form-field";
import {User} from "../../user/user";
import { ids } from 'src/app/ids';

@Component({
  selector: 'app-create-game',
  standalone: true,
  imports: [
    MatButton,
    MatLabel
  ],
  template: `
    <button
      [attr.data-testid]="ids.createGame.OPEN_DIALOG_BUTTON"
      class="rounded"
      mat-raised-button color="primary" (click)="openDialog()"
    >create game
    </button>
  `,
  styleUrl: './create-game.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateGameComponent {
  @Input() user!: User
  readonly dialog!: MatDialog
  private defaultContent!: CreateGameDialogContent
  private currentContent!: CreateGameDialogContent
  constructor(dialog: MatDialog) {
    this.dialog = dialog;
  }

  ngOnChanges(changes: SimpleChanges){
    this.defaultContent = {gameName: "", creatorName: this.user.name}
    this.resetDialogContent()
  }

  resetDialogContent(){
      this.currentContent = {...this.defaultContent}
  }

  openDialog() {
    let params: CreateGameDialogContentParams = {currentContent: this.currentContent, defaultContent: this.defaultContent}
    let dialogRef = this.dialog.open(
      DialogContentComponent,
      {data: params}
    );
    dialogRef.componentRef?.setInput('userId', this.user.id)

    let subscription = dialogRef.componentInstance.resetDialogContentEvent.subscribe(() => {
      this.resetDialogContent();
    });
    dialogRef.afterClosed().subscribe(() => {
      subscription.unsubscribe();
    });

    dialogRef.afterOpened().subscribe(() => {
      document.querySelector("mat-dialog-container")
        ?.setAttribute("data-testid", ids.createGame.DIALOG)
    })
  }

  protected readonly ids = ids;
}

export interface CreateGameDialogContent {
  gameName: string;
  creatorName: string;
}

export interface CreateGameDialogContentParams {
  currentContent: CreateGameDialogContent;
  defaultContent: CreateGameDialogContent;
}
