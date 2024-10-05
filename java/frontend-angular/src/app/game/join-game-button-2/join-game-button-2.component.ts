import {ChangeDetectionStrategy, Component, Input, SimpleChanges} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {ids} from "../../ids";
import {User} from "../../user/user";
import {MatDialog} from "@angular/material/dialog";
import {JoinDialogContentComponent} from "./join-dialog-content/join-dialog-content.component";
import {Identifiable} from "../../common/identifiable";

@Component({
  selector: 'app-join-game-button-2',
  standalone: true,
  imports: [
    MatButton
  ],
  template: `
    @if (canJoin) {
      <button
        [attr.data-testid]="ids.joinGame.openDialogButtonForGameId(gameId)"
        class="rounded"
        mat-stroked-button (click)="openDialog()"
      >join
      </button>
    } @else if (isGameStarted) {
      <span>{{ 'game started' }}</span>
    } @else if (isPlayerInGame) {
      <span>{{ 'already joined' }}</span>
    } @else {
      <span>{{ 'cannot join' }}</span>
    }
    {{ checkRender() }}
  `,
  styleUrl: './join-game-button-2.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class JoinGameButton2Component extends Identifiable {
  protected readonly ids = ids;

  @Input() user!: User
  @Input() gameId!: number
  @Input() playersNames!: string[]
  @Input() canJoin!: boolean;
  @Input() isGameStarted!: boolean;
  @Input() isPlayerInGame!: boolean;
  readonly dialog!: MatDialog
  private defaultContent!: DialogContent
  private currentContent!: DialogContent

  constructor(dialog: MatDialog) {
    super()
    this.dialog = dialog;
  }

  ngOnInit(): void {
    this.resetDialogContent()
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['user']) {
      this.resetDialogContent();
    }
  }

  openDialog() {
    let params: DialogContentParams = {currentContent: this.currentContent, defaultContent: this.defaultContent}
    let dialogRef = this.dialog.open(
      JoinDialogContentComponent,
      {data: params, id: ids.joinGame.DIALOG, ariaLabelledBy: ids.joinGame.DIALOG}
    );
    dialogRef.componentRef?.setInput('userId', this.user.id)
    dialogRef.componentRef?.setInput('gameId', this.gameId)
    dialogRef.componentRef?.setInput('playersNames', this.playersNames)

    dialogRef.afterOpened().subscribe(() => {
      document.querySelector("mat-dialog-container")
        ?.setAttribute("data-testid", ids.joinGame.DIALOG)
    })
  }

  resetDialogContent() {
    this.defaultContent = {playerName: this.user.name}
    this.currentContent = {...this.defaultContent}
  }

}

export interface DialogContent {
  playerName: string;
}

export interface DialogContentParams {
  currentContent: DialogContent;
  defaultContent: DialogContent;
}
