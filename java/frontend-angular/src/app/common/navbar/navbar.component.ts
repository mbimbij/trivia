import {Component} from '@angular/core';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {LogoutButtonComponent} from "./logout-button/logout-button.component";
import {User} from "../../user/user";
import {AsyncPipe, KeyValuePipe, NgIf} from "@angular/common";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {Observable} from "rxjs";
import {ObjectAttributePipe} from "../object-attribute.pipe";
import {Router} from "@angular/router";
import {Identifiable} from "../identifiable";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    MatToolbarModule, MatButtonModule, MatIconModule, LogoutButtonComponent, NgIf, AsyncPipe, ObjectAttributePipe, KeyValuePipe
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})

export class NavbarComponent extends Identifiable {
  user$: Observable<User>;

  constructor(private userService: UserServiceAbstract, protected router: Router) {
    super()
    this.user$ = this.userService.getUser();
  }
}
