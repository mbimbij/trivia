import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatDialog} from "@angular/material/dialog";
import {DialogContentComponent} from "./dialog-content/dialog-content.component";
import {MatLabel} from "@angular/material/form-field";
import {User} from "../../user/user";

export class CreateGameComponentTestIds {
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
      mat-raised-button color="primary" (click)="openDialog()"
      (resetDialogContentEvent)="resetDialogContent()"
    >create game
    </button>
  `,
  styleUrl: './create-game.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateGameComponent {
  @Input() user!: User
  readonly dialog!: MatDialog
  private defaultDialogContent!: CreateGameDialogContent
  private dialogContent!: CreateGameDialogContent

  protected readonly CreateGameComponentTestIds = CreateGameComponentTestIds;

  constructor(dialog: MatDialog) {
    this.dialog = dialog;
  }

  ngOnInit(): void {
    this.defaultDialogContent = {gameName: "", creatorName: this.user.name}
    this.resetDialogContent()
  }

  resetDialogContent(){
      this.dialogContent = {...this.defaultDialogContent}
  }

  openDialog() {
    let dialogRef = this.dialog.open(
      DialogContentComponent,
      {data: this.dialogContent}
    );
    dialogRef.componentRef?.setInput('userId', this.user.id)
    let subscription = dialogRef.componentInstance.resetDialogContentEvent.subscribe(() => {
      this.resetDialogContent();
    });
    dialogRef.afterClosed().subscribe(value => {
      subscription.unsubscribe();
    });
  }
}

export interface CreateGameDialogContent {
  gameName: string;
  creatorName: string;
}
