import {ChangeDetectionStrategy, Component, Input, SimpleChanges} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatDialog} from "@angular/material/dialog";
import {CreateGameDialogContentComponent} from "./dialog-content/create-game-dialog-content.component";
import {MatLabel} from "@angular/material/form-field";
import {User} from "../../user/user";
import {ids} from 'src/app/ids';
import {BaseDialogContentComponent} from "../base-dialog/base-dialog-content/base-dialog-content.component";
import {BaseDialogComponent, BaseDialogData} from "../base-dialog/base-dialog.component";

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
      mat-raised-button color="primary" (click)="openDialog(CreateGameDialogContentComponent)"
    >create game
    </button>
  `,
  styleUrl: './create-game.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateGameComponent extends BaseDialogComponent<CreateGameDialogContentComponent, CreateGameDialogData> {
  protected override changesRequireReset(changes: SimpleChanges): boolean {
    return !!changes['user'];
  }
  @Input() user!: User

  constructor(dialog: MatDialog) {
    super(dialog, ids.createGame.DIALOG)
  }

  protected override resetDataOnChanges() {
    this.resetDefaultData();
    this.resetCreatorName()
  }

  protected resetDefaultData(): void {
    this.defaultData = {gameName: "", creatorName: this.user.name}
  }

  private resetCreatorName() {
    this.data.content.creatorName = this.defaultData.creatorName
  }

  override doAfterOpenDialog() {
    this.dialogRef.componentRef?.setInput('userId', this.user.id)
  }

  protected readonly ids = ids;
  protected readonly CreateGameDialogContentComponent = CreateGameDialogContentComponent;
}

export interface CreateGameDialogData extends BaseDialogData{
  gameName: string;
  creatorName: string;
}
