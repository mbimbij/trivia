import {BaseFormData} from "../../game/base-dialog/base-form.data";
import {Component, Inject, InjectionToken, QueryList, ViewChildren} from "@angular/core";
import {NgModel} from "@angular/forms";

export interface IResettableForm<U extends BaseFormData> {
  get data(): { content: U };
  get defaultData(): U;
  set defaultData(value: U);
  resetData(): void;
}

export const BASE_FORM_DATA = new InjectionToken<any>('the html id of the dialog');
export const BASE_FORM_DEFAULT_DATA = new InjectionToken<any>('the html id of the dialog');


@Component({
  template: '',
  standalone: true,
})
export class ResettableForm<
  U extends BaseFormData
> implements IResettableForm<U> {

  private readonly _data: { content: U }
  private _defaultData?: U;
  private _formControls!: QueryList<NgModel>

  constructor(@Inject(BASE_FORM_DATA) _data: { content: U },
              @Inject(BASE_FORM_DEFAULT_DATA) _defaultData?: U) {
    this._data = _data

    if(!!_defaultData){
      this._defaultData = _defaultData
    }else {
      this._defaultData = {..._data.content}
    }
  }

  get data(): { content: U } {
    return this._data;
  }

  get defaultData(): U {
    return this._defaultData!;
  }

  set defaultData(value: U) {
    this._defaultData = value;
  }

  set formControls(value: QueryList<NgModel>) {
    this._formControls = value;
  }

  resetData() {
    this._data.content = {...this._defaultData!}
    this.markInputsAsPristine();
  }

  markInputsAsPristine() {
    this._formControls.forEach(item => item.control.markAsPristine())
  }
}
