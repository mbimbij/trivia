import {Component, Inject, Input} from '@angular/core';
import {Identifiable} from "../../../common/identifiable";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {BaseDialogData} from "../base-dialog.component";

@Component({
  selector: 'app-base-dialog-content',
  standalone: true,
  template: '<p>base-dialog-content works!</p>',
  styleUrl: './base-dialog-content.component.css'
})
export class BaseDialogContentComponent<
  T extends BaseDialogContentComponent<any, any>,
  U extends BaseDialogData
> extends Identifiable {
  @Input() defaultData!: U

  constructor(protected matDialogRef: MatDialogRef<T>,
              @Inject(MAT_DIALOG_DATA) public data: { content: U }) {
    super()
  }

  protected closeDialogAndResetData(){
    this.resetData()
    this.matDialogRef.close()
  }

  protected resetData() {
    this.data.content = {...this.defaultData}
  }
}
