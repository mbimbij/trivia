import {Component, Inject, InjectionToken, SimpleChanges} from '@angular/core';
import {Identifiable} from "../../common/identifiable";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {ComponentType} from "@angular/cdk/overlay";
import {BaseDialogContentComponent} from "./base-dialog-content/base-dialog-content.component";

export const HTML_ID_TOKEN = new InjectionToken<string>('the html id of the dialog');

@Component({
  selector: 'app-base-dialog',
  standalone: true,
  imports: [],
  template: '<p>base-dialog works!</p>',
  styleUrl: './base-dialog.component.css'
})
export abstract class BaseDialogComponent<
  T extends BaseDialogContentComponent<any, any>,
  U extends BaseDialogData
> extends Identifiable {
  protected data = {
    content: {} as U
  }
  protected defaultData!: U
  readonly dialog!: MatDialog
  protected htmlId: string

  protected dialogRef!: MatDialogRef<T>

  protected constructor(dialog: MatDialog, @Inject(HTML_ID_TOKEN) htmlId: string) {
    super()
    this.dialog = dialog;
    this.htmlId = htmlId;
  }

  ngOnChanges(changes: SimpleChanges) {
    if (this.changesRequireReset(changes)) {
      this.resetDataOnChanges();
    }
  }

  protected resetDataOnChanges() {
    this.resetAllData();
  }

  protected abstract changesRequireReset(changes: SimpleChanges): boolean

  ngOnInit(): void {
    this.resetAllData()
  }

  private resetAllData() {
    this.resetDefaultData();
    this.resetData();
  }

  protected abstract resetDefaultData(): void;

  protected resetData() {
    this.data.content = {...this.defaultData}
  }

  protected openDialog(componentType: ComponentType<T>) {
    this.dialogRef = this.dialog.open<T>(
      componentType,
      {data: this.data, id: this.htmlId, ariaLabelledBy: this.htmlId}
    );
    this.dialogRef.afterOpened().subscribe(() => {
      document.querySelector("mat-dialog-container")
        ?.setAttribute("data-testid", this.htmlId)
    })
    this.dialogRef.componentRef?.setInput('defaultData', this.defaultData)
    this.doAfterOpenDialog();
  }

  protected abstract doAfterOpenDialog(): void;
}

export interface BaseDialogData {
}
