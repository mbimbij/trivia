import {ChangeDetectionStrategy, Component} from '@angular/core';
import {AsyncPipe, NgIf} from "@angular/common";
import {MatIcon} from "@angular/material/icon";
import {Identifiable} from "../../identifiable";
import {UserServiceAbstract} from "../../../services/user-service.abstract";
import {AuthenticationServiceAbstract} from "../../../services/authentication-service-abstract";
import {NavbarLogoutButtonComponent} from "./navbar-logout-button/navbar-logout-button.component";
import {Router} from "@angular/router";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-navbar-user',
  standalone: true,
  imports: [
    AsyncPipe,
    NavbarLogoutButtonComponent,
    MatIcon,
    NgIf,
    MatButton
  ],
  templateUrl: './navbar-user.component.html',
  styleUrl: './navbar-user.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NavbarUserComponent extends Identifiable {

  constructor(protected userService: UserServiceAbstract,
              protected authenticationService: AuthenticationServiceAbstract,
              protected router: Router) {
    super()
  }
}
