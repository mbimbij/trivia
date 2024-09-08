import {ChangeDetectionStrategy, ChangeDetectorRef, Component} from '@angular/core';
import {AsyncPipe, NgIf} from "@angular/common";
import {Router} from "@angular/router";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {ConsoleLogPipe} from "../../../console-log.pipe";
import {AuthenticationServiceAbstract} from "../../../services/authentication-service-abstract";
import {Identifiable} from "../../identifiable";
import {flatMap, mergeMap, Observable} from "rxjs";
import {fromPromise} from "rxjs/internal/observable/innerFrom";

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
    <button mat-icon-button class="example-icon" aria-label="logout"
            (click)="authenticationService.logout()"
    >
      <mat-icon>logout</mat-icon>
    </button>
    {{ checkRender() }}
  `,
  styleUrl: './logout-button.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LogoutButtonComponent extends Identifiable {

  constructor(private router: Router,
              protected authenticationService: AuthenticationServiceAbstract) {
    super()
  }
}
