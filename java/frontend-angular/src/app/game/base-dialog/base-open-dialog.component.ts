import {Component, Inject, InjectionToken, SimpleChanges} from '@angular/core';
import {IdentifiableImpl} from "../../shared/identifiableImpl";
import {MatDialog, MatDialogRef} from "@angular/material/dialog";
import {ComponentType} from "@angular/cdk/overlay";
import {BaseDialogContentComponent} from "./base-dialog-content.component";
import {BaseFormData} from "./base-form.data";
import {ResettableForm} from "../base-resettable-form/resettable-form";

export const HTML_ID_TOKEN = new InjectionToken<string>('the html id of the dialog');

@Component({
  selector: 'app-base-dialog',
  standalone: true,
  imports: [],
  template: `<p>base-dialog works!</p>`,
  styleUrl: './base-open-dialog.component.css'
})
export abstract class BaseOpenDialogComponent<
  T extends BaseDialogContentComponent<any, any>,
  U extends BaseFormData
> extends IdentifiableImpl {
  protected data = {
    content: {} as U
  }
  protected defaultData!: U

  protected constructor(private readonly dialog: MatDialog,
                        @Inject(HTML_ID_TOKEN) private readonly htmlId: string) {
    super()
  }

  ngOnChanges(changes: SimpleChanges) {
    if (this.changesRequireReset(changes)) {
      this.resetDefaultData();
      this.doAdditionalResetOnChanges();
    }
  }
  protected abstract changesRequireReset(changes: SimpleChanges): boolean

  ngOnInit(): void {
    this.resetDefaultData();
    this.resetData();
  }

  protected doAdditionalResetOnChanges() {
  }

  protected abstract resetDefaultData(): void;

  protected resetData() {
    this.data.content = {...this.defaultData}
  }

  protected openDialog(componentType: ComponentType<T>) {
    let dialogRef = this.dialog.open<T>(
      componentType,
      {data: this.data, id: this.htmlId, ariaLabelledBy: this.htmlId}
    );
    dialogRef.afterOpened().subscribe(() => {
      document.querySelector("mat-dialog-container")
        ?.setAttribute("data-testid", this.htmlId)
    })
    // dialogRef.componentRef?.setInput('defaultData', this.defaultData)
    dialogRef.componentInstance.defaultData = this.defaultData
    this.doAfterOpenDialog(dialogRef);
  }

  protected abstract doAfterOpenDialog(dialogRef: MatDialogRef<T, any>): void;
}

