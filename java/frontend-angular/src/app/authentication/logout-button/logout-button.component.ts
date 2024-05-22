import {Component} from '@angular/core';
import {NgIf} from "@angular/common";
import {Router} from "@angular/router";
import {Observable} from "rxjs";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {AuthenticationService} from "../authentication.service";

@Component({
  selector: 'app-logout-button',
  standalone: true,
  imports: [
    NgIf,
    MatIcon,
    MatIconButton
  ],
  templateUrl: './logout-button.component.html',
  styleUrl: './logout-button.component.css'
})
export class LogoutButtonComponent {

  constructor(private router: Router,
              private authenticationService: AuthenticationService) {
  }

  logout() {
    this.authenticationService.logout();
    this.router.navigate(['/authentication'])
  }

  isLoggedIn():Observable<boolean> {
    return this.authenticationService.isLoggedIn
  }
}
