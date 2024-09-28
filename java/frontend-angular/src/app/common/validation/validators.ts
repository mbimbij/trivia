import {AbstractControl} from "@angular/forms";

export function notBlankValidator(control: AbstractControl) {
  return (control.value || '').trim().length? null : { 'notBlank': true };
}
