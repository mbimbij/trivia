import {ChangeDetectionStrategy, Component, Input, SimpleChanges} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {CreateGameDialogContentComponent} from "./dialog-content/create-game-dialog-content.component";
import {MatLabel} from "@angular/material/form-field";
import {User} from "../../user/user";
import {ids} from 'src/app/ids';
import {BaseOpenDialogComponent} from "../base-dialog/base-open-dialog.component";
import {CreateGameDialogData} from "./create-game-dialog.data";

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
  styleUrl: './open-create-game.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class OpenCreateGameComponent extends BaseOpenDialogComponent<CreateGameDialogContentComponent, CreateGameDialogData> {
  protected override changesRequireReset(changes: SimpleChanges): boolean {
    return !!changes['user'];
  }
  @Input() user!: User

  constructor(dialog: MatDialog) {
    super(dialog, ids.createGame.DIALOG)
  }

  protected override doAdditionalResetOnChanges() {
    this.data.content.creatorName = this.defaultData.creatorName
  }

  protected resetDefaultData(): void {
    this.defaultData = {gameName: "", creatorName: this.user.name}
  }

  override doAfterOpenDialog(dialogRef: MatDialogRef<CreateGameDialogContentComponent, any>): void {
    dialogRef.componentRef?.setInput('userId', this.user.id)
  }

  protected readonly ids = ids;
  protected readonly CreateGameDialogContentComponent = CreateGameDialogContentComponent;
}

