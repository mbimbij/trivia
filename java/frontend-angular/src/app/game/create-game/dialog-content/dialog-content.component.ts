import {ChangeDetectionStrategy, Component, EventEmitter, Inject, Input, Output} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {Identifiable} from "../../../common/identifiable";
import {UserDto} from "../../../openapi-generated/game";
import {MatIcon} from "@angular/material/icon";
import {FormsModule} from "@angular/forms";
import {CreateGameComponentTestIds, CreateGameDialogContent} from "../create-game.component";

@Component({
  selector: 'app-dialog-content',
  standalone: true,
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatButton,
    MatDialogClose,
    MatFormField,
    MatInput,
    MatLabel,
    MatIcon,
    FormsModule
  ],
  templateUrl: './dialog-content.component.html',
  styleUrl: './dialog-content.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DialogContentComponent extends Identifiable {

  @Input() userId!: string
  @Output() resetDialogContentEvent = new EventEmitter<null>();

  constructor(private matDialogRef: MatDialogRef<DialogContentComponent>,
              private gameService: GameServiceAbstract,
              @Inject(MAT_DIALOG_DATA) public data: CreateGameDialogContent) {
    super()
  }

  protected createGame() {
    console.log(`create game ${this.data.gameName} with name ${this.data.creatorName}`)
    let creator = {name: this.data.creatorName, id: this.userId} as UserDto
    this.gameService.create(this.data.gameName, creator).subscribe({
      next: newGame => {
        console.log(`created game: ${newGame.id}`)
        this.resetDialogContentEvent.next(null)
        this.matDialogRef.close()
      }
    })
  }

  protected readonly CreateGameComponentTestIds = CreateGameComponentTestIds;
}
