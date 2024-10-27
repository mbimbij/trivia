import {Component, Inject, QueryList, SimpleChanges, ViewChildren} from '@angular/core';
import {Identifiable} from "../../shared/identifiable";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BehaviorSubject, Observable} from "rxjs";
import {BaseFormData} from "./base-form.data";
import {IResettableForm, ResettableForm} from "../../shared/resettable-form/resettable-form";
import {NgModel} from "@angular/forms";

@Component({
  standalone: true,
  template: '<p>base-dialog-content works!</p>',
  styleUrls: ['./base-dialog-content.component.css', '../../shared/resettable-form/resettable-form.css']
})
export abstract class BaseDialogContentComponent<
  T extends BaseDialogContentComponent<any, any>,
  U extends BaseFormData
> extends Identifiable implements IResettableForm<U> {
  protected formData!: ResettableForm<U>
  @ViewChildren(NgModel) formControls!: QueryList<NgModel>;
  protected backendErrorMessage$ = new BehaviorSubject<string | null>(null);

  protected constructor(protected matDialogRef: MatDialogRef<T>,
                        @Inject(MAT_DIALOG_DATA) data: { content: U }) {
    super()
    this.formData = new ResettableForm(data);
  }

  get defaultData(): U {
    return this.formData.defaultData;
  }

  set defaultData(value: U) {
    this.formData.defaultData = value;
  }

  ngOnChanges(changes: SimpleChanges) {
    if (changes['defaultData']) {
      this.formData.defaultData = changes['defaultData'].currentValue;
    }
  }

  ngAfterViewInit() {
    this.formData.formControls = this.formControls;
  }

  get data(): { content: U } {
    return this.formData.data;
  }

  private handleSuccess = (response: any) => {
    this.resetData()
    this.backendErrorMessage$.next(null)
    this.matDialogRef.close()
    this.doAdditionalActionsOnSuccess(response)
  }

  resetData() {
    this.formData.resetData()
    this.backendErrorMessage$.next(null)
  }

  protected callBackendOnSubmit() {
    this.doCallBackend().subscribe({
      next: this.handleSuccess,
      error: this.handleError
    })
  }

  protected abstract doCallBackend(): Observable<any>;

  protected doAdditionalActionsOnSuccess(response: any) {
  }

  private handleError = (err: any) => {
    this.backendErrorMessage$.next(err.message)
  }

}
