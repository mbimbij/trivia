import {ChangeDetectionStrategy, Component, Input, SimpleChanges} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {ids} from "../../ids";
import {User} from "../../user/user";
import {MatDialog} from "@angular/material/dialog";
import {BaseDialogComponent, BaseDialogData} from "../base-dialog/base-dialog.component";
import {JoinDialogContentComponent} from "./join-dialog-content/join-dialog-content.component";

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
        mat-stroked-button (click)="openDialog(JoinDialogContentComponent)"
      >join
      </button>
    } @else if (isGameStarted) {
      <span>{{ 'game started' }}</span>
    } @else if (isPlayerInGame) {
      <span>{{ 'already joined' }}</span>
    } @else {
      <span>{{ 'cannot join' }}</span>
    }
  `,
  styleUrl: './join-game-button-2.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class JoinGameButton2Component extends BaseDialogComponent<JoinDialogContentComponent, JoinDialogData> {
  protected readonly ids = ids;

  @Input() user!: User
  @Input() gameId!: number
  @Input() playersNames!: string[]
  @Input() canJoin!: boolean;
  @Input() isGameStarted!: boolean;
  @Input() isPlayerInGame!: boolean;

  constructor(dialog: MatDialog) {
    super(dialog,ids.joinGame.DIALOG);
  }

  protected override changesRequireReset(changes: SimpleChanges) {
    return !!changes['user'];
  }

  protected override doAfterOpenDialog() {
    this.dialogRef.componentRef?.setInput('userId', this.user.id)
    this.dialogRef.componentRef?.setInput('gameId', this.gameId)
    this.dialogRef.componentRef?.setInput('playersNames', this.playersNames)
  }

  protected override resetDefaultData(): void {
    this.defaultData = {playerName: this.user.name}
  }

  protected readonly JoinDialogContentComponent = JoinDialogContentComponent;
}

export interface JoinDialogData extends BaseDialogData {
  playerName: string;
}
