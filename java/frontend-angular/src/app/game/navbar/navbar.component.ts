import {Component} from '@angular/core';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {LogoutButtonComponent} from "./logout-button/logout-button.component";
import {User} from "../../user/user";
import {NgIf} from "@angular/common";
import {UserServiceAbstract} from "../../user/user-service.abstract";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    MatToolbarModule, MatButtonModule, MatIconModule, LogoutButtonComponent, NgIf
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})

export class NavbarComponent {
  user!: User

  constructor(private userService:UserServiceAbstract) {
    this.user = this.userService.getUser();
    userService.registerUserUpdatedObserver(this.updateUser)
  }

  private updateUser = (updatedUser: User) => {
    this.user = updatedUser;
  }
}
