import {Component, Input, OnChanges, SimpleChanges} from '@angular/core';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {LogoutButtonComponent} from "./logout-button/logout-button.component";
import {UserService} from "../../user/user.service";
import {User} from "../../user/user";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    MatToolbarModule, MatButtonModule, MatIconModule, LogoutButtonComponent, NgIf
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})

// TODO try to update username with `implements OnChanges` rather
export class NavbarComponent {
  user!: User

  constructor(private userService:UserService) {
    this.user = this.userService.getUser();
    userService.registerUserUpdatedObserver(this.updateUser)
  }

  private updateUser = (updatedUser: User) => {
    this.user = updatedUser;
  }
}
