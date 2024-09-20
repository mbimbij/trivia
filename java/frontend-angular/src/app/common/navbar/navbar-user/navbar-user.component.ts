import {ChangeDetectionStrategy, Component} from '@angular/core';
import {AsyncPipe, NgIf} from "@angular/common";
import {LogoutButtonComponent} from "./logout-button/logout-button.component";
import {MatIcon} from "@angular/material/icon";
import {Identifiable} from "../../identifiable";
import {UserServiceAbstract} from "../../../services/user-service.abstract";
import {AuthenticationServiceAbstract} from "../../../services/authentication-service-abstract";

@Component({
  selector: 'app-navbar-user',
  standalone: true,
  imports: [
    AsyncPipe,
    LogoutButtonComponent,
    MatIcon,
    NgIf
  ],
  templateUrl: './navbar-user.component.html',
  styleUrl: './navbar-user.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NavbarUserComponent extends Identifiable {

  constructor(protected userService: UserServiceAbstract,
              protected authenticationService: AuthenticationServiceAbstract) {
    super()
  }
}
