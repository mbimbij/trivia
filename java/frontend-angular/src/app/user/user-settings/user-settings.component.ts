import {ChangeDetectionStrategy, Component, QueryList, ViewChildren} from '@angular/core';
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {Identifiable} from "../../shared/identifiable";
import {AsyncPipe, NgIf} from "@angular/common";
import {ids} from "../../ids";
import {MatError, MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {NotBlankValidatorDirective} from "../../shared/validation/not-blank-validator.directive";
import {FormsModule, NgModel, ReactiveFormsModule} from "@angular/forms";
import {ValidationErrorCodes} from "../../shared/validation/validation-error-codes";
import {MatDialogActions} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";
import {IResettableForm, ResettableForm} from "../../shared/resettable-form/resettable-form";
import {MatSnackBar, MatSnackBarConfig} from "@angular/material/snack-bar";

@Component({
    selector: 'app-user-settings',
    standalone: true,
    imports: [
        NgIf,
        AsyncPipe,
        MatInput,
        NotBlankValidatorDirective,
        ReactiveFormsModule,
        FormsModule,
        MatError,
        MatFormField,
        MatLabel,
        MatDialogActions,
        MatButton
    ],
    templateUrl: './user-settings.component.html',
    styleUrls: ['./user-settings.component.css'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserSettingsComponent extends Identifiable implements IResettableForm<any> {
    protected formData!: ResettableForm<any>
    @ViewChildren(NgModel) formControls!: QueryList<NgModel>;

    constructor(protected userService: UserServiceAbstract, private snackbar: MatSnackBar) {
        super()
        this.formData = new ResettableForm({content: {username: ''}});
    }

    ngOnInit(): void {
        this.userService.getUser().subscribe(value => {
            this.formData.data.content = {username: value.name}
            this.formData.defaultData = {username: value.name}
        })
    }

    ngAfterViewInit() {
        this.formData.formControls = this.formControls;
    }

    get data(): { content: any; } {
        return this.formData.data;
    }

    get defaultData(): any {
        return this.formData.defaultData;
    }

    set defaultData(value: any) {
        this.formData.defaultData = value;
    }

    resetData(): void {
        this.formData.resetData()
    }

    submitForm(newUserName: string) {
        this.userService.renameUser(newUserName)
            .subscribe({
                next: () => this.handleSuccess(),
                error: () => this.handleError()
            })
    }

    private handleSuccess() {
        let config = {
            duration: 2500,
            horizontalPosition: "right",
            verticalPosition: "top",
            panelClass: "success-snackbar"
        } as MatSnackBarConfig;
        this.snackbar.open("Changes applied successfully", "OK", config)
        this.formData.markInputsAsPristine()
    }

    private handleError() {
        let config = {
            duration: 2500,
            horizontalPosition: "right",
            verticalPosition: "top",
            panelClass: "error-snackbar"
        } as MatSnackBarConfig;
        this.snackbar.open("An error occured", "OK", config)
    }

    protected readonly ids = ids;

    protected readonly ValidationErrorCodes = ValidationErrorCodes;
}
