import {ChangeDetectionStrategy, Component, EventEmitter, Inject, Input} from '@angular/core';
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {UserDto} from "../../../openapi-generated/game";
import {Identifiable} from "../../../common/identifiable";
import {ids} from 'src/app/ids';
import {MatButton} from "@angular/material/button";
import {MatDivider} from "@angular/material/divider";
import {MatError, MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {NgIf} from "@angular/common";
import {NotBlankValidatorDirective} from "../../../common/validation/not-blank-validator.directive";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {DialogContent, DialogContentParams} from "../join-game-button-2.component";
import {NotDuplicateValidatorDirective} from "../not-duplicate-validator.directive";
import {ValidationErrorCodes} from "../../../common/validation/validation-error-codes";

@Component({
  selector: 'app-join-dialog-content',
  standalone: true,
  imports: [
    MatButton,
    MatDialogActions,
    MatDialogClose,
    MatDialogContent,
    MatDialogTitle,
    MatDivider,
    MatError,
    MatFormField,
    MatInput,
    MatLabel,
    NgIf,
    NotBlankValidatorDirective,
    ReactiveFormsModule,
    FormsModule,
    NotDuplicateValidatorDirective
  ],
  templateUrl: './join-dialog-content.component.html',
  styleUrl: './join-dialog-content.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class JoinDialogContentComponent extends Identifiable {
  @Input() userId!: string
  @Input() gameId!: number
  @Input() playersNames!: string[]
  currentContent!: DialogContent
  defaultContent!: DialogContent
  resetDialogContentEvent = new EventEmitter<null>();

  constructor(private matDialogRef: MatDialogRef<JoinDialogContentComponent>,
              private gameService: GameServiceAbstract,
              @Inject(MAT_DIALOG_DATA) public data: DialogContentParams) {
    super()
    this.currentContent = data.currentContent;
    this.defaultContent = data.defaultContent;
  }

  protected joinGame() {
    let creator = {name: this.currentContent.playerName, id: this.userId} as UserDto
    this.gameService.join(this.gameId, creator).subscribe({
      next: () => {
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
  protected readonly ValidationErrorCodes = ValidationErrorCodes;
}
