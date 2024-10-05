import {AbstractControl} from "@angular/forms";
import {ValidationErrorCodes} from "./validation-error-codes";

export function notBlankValidator(control: AbstractControl) {
  return (control.value || '').trim().length? null : { [ValidationErrorCodes.NOT_BLANK]: true };
}
