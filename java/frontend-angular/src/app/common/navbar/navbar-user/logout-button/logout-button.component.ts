import {ChangeDetectionStrategy, Component, ViewContainerRef} from '@angular/core';
import {AsyncPipe, NgIf} from "@angular/common";
import {Router} from "@angular/router";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {ConsoleLogPipe} from "../../../../console-log.pipe";
import {Identifiable} from "../../../identifiable";
import {AuthenticationServiceAbstract} from "../../../../services/authentication-service-abstract";

@Component({
  selector: 'app-logout-button',
  standalone: true,
  imports: [
    NgIf,
    MatIcon,
    MatIconButton,
    AsyncPipe,
    ConsoleLogPipe
  ],
  template: `
    <button mat-icon-button class="example-icon" aria-label="logout" (click)="authenticationService.logout()">
      <mat-icon>logout</mat-icon>
    </button>
    {{ checkRender() }}
  `,
  styleUrl: './logout-button.component.css'
})
export class LogoutButtonComponent extends Identifiable {

  constructor(protected authenticationService: AuthenticationServiceAbstract) {
    super()
  }
}
