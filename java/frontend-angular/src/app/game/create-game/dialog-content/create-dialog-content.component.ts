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
import {CreateGameDialogContent} from "../create-game.component";
import {NgIf} from "@angular/common";
import {NotBlankValidatorDirective} from "../../../common/validation/not-blank-validator.directive";
import {MatDivider} from "@angular/material/divider";
import {ids} from 'src/app/ids';
import {ValidationErrorCodes} from "../../../common/validation/validation-error-codes";

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
  templateUrl: './create-dialog-content.component.html',
  styleUrl: './create-dialog-content.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CreateDialogContentComponent extends Identifiable {
  @Input() userId!: string
  resetEvent = new EventEmitter<null>();

  constructor(private matDialogRef: MatDialogRef<CreateDialogContentComponent>,
              private gameService: GameServiceAbstract,
              @Inject(MAT_DIALOG_DATA) public data: CreateGameDialogContent) {
    super()
  }

  protected createGame() {
    let creator = {name: this.data.creatorName, id: this.userId} as UserDto
    this.gameService.create(this.data.gameName, creator).subscribe({
      next: newGame => {
        console.log(`created game: ${newGame.id}`)
        this.resetData()
        this.matDialogRef.close()
      }
    })
  }

  protected resetData() {
    this.resetEvent.next(null)
  }

  protected readonly ids = ids;
  protected readonly ValidationErrorCodes = ValidationErrorCodes;
}
