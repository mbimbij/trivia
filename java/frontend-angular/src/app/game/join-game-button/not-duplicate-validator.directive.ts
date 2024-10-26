import {Directive, Input} from '@angular/core';
import {IdentifiableImpl} from "../../shared/identifiableImpl";
import {AbstractControl, NG_VALIDATORS, ValidationErrors, Validator} from "@angular/forms";
import {ValidationErrorCodes} from "../../shared/validation/validation-error-codes";

@Directive({
  selector: 'input[notDuplicate]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: NotDuplicateValidatorDirective,
      multi: true,
    },
  ],
  standalone: true
})
export class NotDuplicateValidatorDirective extends IdentifiableImpl implements Validator {
  @Input('notDuplicate') playersNames!: string[];

  constructor() {
    super()
  }

  validate(control: AbstractControl<any, any>): ValidationErrors | null {
    let input = (control.value || '').trim();
    return this.playersNames.includes(input)
      ? {[ValidationErrorCodes.DUPLICATE_PLAYER_NAME]: true}
      : null
  }
}


