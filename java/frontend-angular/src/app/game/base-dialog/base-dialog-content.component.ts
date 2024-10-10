import {Component, Inject, Input} from '@angular/core';
import {Identifiable} from "../../common/identifiable";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BehaviorSubject, Observable} from "rxjs";
import {BaseDialogData} from "./base-dialog.data";

@Component({
  selector: 'app-base-dialog-content',
  standalone: true,
  template: '<p>base-dialog-content works!</p>',
  styleUrl: './base-dialog-content.component.css'
})
export abstract class BaseDialogContentComponent<
  T extends BaseDialogContentComponent<any, any>,
  U extends BaseDialogData
> extends Identifiable {
  @Input() defaultData!: U
  protected backendErrorMessage$ = new BehaviorSubject<string | null>(null);

  protected constructor(protected matDialogRef: MatDialogRef<T>,
                        @Inject(MAT_DIALOG_DATA) public data: { content: U }) {
    super()
  }

  private handleBackendSuccess = (response: any) => {
    this.resetData()
    this.backendErrorMessage$.next(null)
    this.matDialogRef.close()
    this.doAdditionalActionsOnBackendSuccess(response)
  }

  protected resetData() {
    this.data.content = {...this.defaultData}
    this.backendErrorMessage$.next(null)
  }

  protected doOnFormSubmit() {
    this.onFormSubmitFunction().subscribe({
      next: this.handleBackendSuccess,
      error: this.handleBackendError
    })
  }

  protected abstract onFormSubmitFunction(): Observable<any>;

  protected doAdditionalActionsOnBackendSuccess(response: any) {}

  private handleBackendError = (err: any) => {
    this.backendErrorMessage$.next(err.message)
  }

}
