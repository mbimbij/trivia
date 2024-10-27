import {ChangeDetectionStrategy, Component} from '@angular/core';
import {AsyncPipe, NgIf} from "@angular/common";
import {MatIcon} from "@angular/material/icon";
import {Identifiable} from "../../identifiable";
import {UserServiceAbstract} from "../../../services/user-service.abstract";
import {AuthenticationServiceAbstract} from "../../../services/authentication-service-abstract";
import {Router} from "@angular/router";
import {MatButton, MatIconButton} from "@angular/material/button";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";

@Component({
  selector: 'app-navbar-user',
  standalone: true,
  imports: [
    AsyncPipe,
    MatIcon,
    NgIf,
    MatButton,
    MatMenu,
    MatMenuItem,
    MatMenuTrigger,
    MatIconButton
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
