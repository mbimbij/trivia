import {ChangeDetectionStrategy, Component, Input, SimpleChanges} from '@angular/core';
import {MatButton} from "@angular/material/button";
import {MatDialog} from "@angular/material/dialog";
import {CreateGameDialogContentComponent} from "./dialog-content/create-game-dialog-content.component";
import {MatLabel} from "@angular/material/form-field";
import {User} from "../../user/user";
import {ids} from 'src/app/ids';

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
  private defaultData!: CreateGameDialogContent
  private data = {} as CreateGameDialogContent

  constructor(dialog: MatDialog) {
    this.dialog = dialog;
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['user']) {
      this.resetDefaultData();
      this.resetCreatorName()
    }
  }

  ngOnInit(): void {
    this.resetDefaultData();
    this.resetData();
  }

  private resetDefaultData() {
    this.defaultData = {gameName: "", creatorName: this.user.name}
  }

  private resetData() {
    this.resetGameName();
    this.resetCreatorName();
  }

  private resetCreatorName() {
    this.data.creatorName = this.defaultData.creatorName
  }

  private resetGameName() {
    this.data.gameName = this.defaultData.gameName
  }

  openDialog() {
    let dialogRef = this.dialog.open(
      CreateGameDialogContentComponent,
      {data: this.data, id: ids.createGame.DIALOG, ariaLabelledBy: ids.createGame.DIALOG}
    );
    dialogRef.componentRef?.setInput('userId', this.user.id)
    dialogRef.componentRef?.setInput('defaultData', this.defaultData)

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
