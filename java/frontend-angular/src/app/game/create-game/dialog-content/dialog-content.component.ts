import {ChangeDetectionStrategy, Component, EventEmitter, Inject, Input} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {Identifiable} from "../../../common/identifiable";
import {UserDto} from "../../../openapi-generated/game";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {
  CreateGameDialogContent,
  CreateGameDialogContentParams
} from "../create-game.component";
import {NgIf} from "@angular/common";
import {NotBlankValidatorDirective} from "../../../common/validation/not-blank-validator.directive";
import {MatDivider} from "@angular/material/divider";
import { ids } from 'src/app/ids';

@Component({
  selector: 'app-dialog-content',
  standalone: true,
  imports: [
    MatDialogTitle,
    MatError,
    ReactiveFormsModule,
    MatLabel,
    MatFormField,
    MatDialogContent,
    FormsModule,
    MatInput,
    MatDialogActions,
    MatButton,
    MatDialogClose,
    NgIf,
    NotBlankValidatorDirective,
    MatDivider
  ],
  templateUrl: './dialog-content.component.html',
  styleUrl: './dialog-content.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DialogContentComponent extends Identifiable {
  @Input() userId!: string
  currentContent!: CreateGameDialogContent
  defaultContent!: CreateGameDialogContent
  resetDialogContentEvent = new EventEmitter<null>();

  constructor(private matDialogRef: MatDialogRef<DialogContentComponent>,
              private gameService: GameServiceAbstract,
              @Inject(MAT_DIALOG_DATA) public data: CreateGameDialogContentParams) {
    super()
    this.currentContent = data.currentContent;
    this.defaultContent = data.defaultContent;
  }

  protected createGame() {
    let creator = {name: this.currentContent.creatorName, id: this.userId} as UserDto
    this.gameService.create(this.currentContent.gameName, creator).subscribe({
      next: newGame => {
        console.log(`created game: ${newGame.id}`)
        this.resetDialogContent()
        this.matDialogRef.close()
      }
    })
  }

  resetDialogContent() {
    this.currentContent = {...this.defaultContent}
    this.resetDialogContentEvent.next(null)
  }

  protected readonly ids = ids;
}
