import {Component, Input} from '@angular/core';
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {Identifiable} from "../../shared/identifiable";
import {AsyncPipe, NgIf} from "@angular/common";
import {ids} from "../../ids";
import {MatError, MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {NotBlankValidatorDirective} from "../../shared/validation/not-blank-validator.directive";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ValidationErrorCodes} from "../../shared/validation/validation-error-codes";
import {MatDialogActions} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-user-settings',
  standalone: true,
  imports: [
    NgIf,
    AsyncPipe,
    MatInput,
    NotBlankValidatorDirective,
    ReactiveFormsModule,
    FormsModule,
    MatError,
    MatFormField,
    MatLabel,
    MatDialogActions,
    MatButton
  ],
  templateUrl: './user-settings.component.html',
  styleUrl: './user-settings.component.css'
})
export class UserSettingsComponent extends Identifiable{

  constructor(protected userService: UserServiceAbstract) {
    super()
  }

  protected readonly ids = ids;
  protected readonly ValidationErrorCodes = ValidationErrorCodes;
}
