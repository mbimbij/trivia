import {Directive} from '@angular/core';
import {AbstractControl, NG_VALIDATORS, ValidationErrors, Validator} from "@angular/forms";
import {IdentifiableImpl} from "../identifiableImpl";
import {notBlankValidator} from "./validators";

@Directive({
  selector: 'input[notBlank]',
  providers: [
    {
      provide: NG_VALIDATORS,
      useExisting: NotBlankValidatorDirective,
      multi: true,
    },
  ],
  standalone: true
})
export class NotBlankValidatorDirective extends IdentifiableImpl implements Validator {

  constructor() {
    super()
  }

  validate(control: AbstractControl<any, any>): ValidationErrors | null {
    return notBlankValidator(control)
  }

}
