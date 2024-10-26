import {BaseFormData} from "../base-dialog/base-form.data";

export interface IResettableForm<U extends BaseFormData> {
  get data(): { content: U };
  get defaultData(): U;
  set defaultData(value: U);
  resetData(): void;
}

export class ResettableForm<
  U extends BaseFormData
> implements IResettableForm<U> {

  private readonly _data: { content: U }
  private _defaultData?: U;

  constructor(_data: { content: U }, _defaultData?: U) {
    this._data = _data
    this._defaultData = _defaultData
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

  resetData() {
    this._data.content = {...this._defaultData!}
  }

}
