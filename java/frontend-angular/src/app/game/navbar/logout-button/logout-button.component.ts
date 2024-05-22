import {Component} from '@angular/core';
import {AsyncPipe, NgIf} from "@angular/common";
import {Router} from "@angular/router";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {AuthenticationService} from "../../../authentication/authentication.service";
import {ConsoleLogPipe} from "../../../console-log.pipe";

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
            (click)="logout()"
            *ngIf="authenticationService.loggedIn"
    >
      <mat-icon>logout</mat-icon>
    </button>
  `,
  styleUrl: './logout-button.component.css'
})
export class LogoutButtonComponent {

  constructor(private router: Router,
              protected authenticationService: AuthenticationService) {
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/authentication'])
  }
}
