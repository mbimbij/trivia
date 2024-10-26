import {Component, Input} from '@angular/core';
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {IdentifiableImpl} from "../../shared/identifiableImpl";
import {AsyncPipe, NgIf} from "@angular/common";
import {ids} from "../../ids";
import {MatError, MatFormField, MatInput, MatLabel} from "@angular/material/input";
import {NotBlankValidatorDirective} from "../../shared/validation/not-blank-validator.directive";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ValidationErrorCodes} from "../../shared/validation/validation-error-codes";
import {MatDialogActions} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";
import {IResettableForm, ResettableForm} from "../../game/base-resettable-form/resettable-form";
import {user} from "@angular/fire/auth";

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
    styleUrl: './user-settings.component.css'
})
export class UserSettingsComponent extends IdentifiableImpl implements IResettableForm<any> {

    protected formData!: ResettableForm<any>

    constructor(protected userService: UserServiceAbstract) {
        super()
    }

    ngOnInit(): void {
        this.userService.getUser().subscribe(value => {
            let content = {username: value.name};
            this.formData = new ResettableForm({content: content})
        })
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

    protected readonly ids = ids;
    protected readonly ValidationErrorCodes = ValidationErrorCodes;
}
