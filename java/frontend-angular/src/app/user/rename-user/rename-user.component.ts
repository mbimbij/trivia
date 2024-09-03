import { Component } from '@angular/core';
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {Identifiable} from "../../common/identifiable";
import {Observable} from "rxjs";
import {User} from "../user";
import {AsyncPipe, NgIf} from "@angular/common";

@Component({
  selector: 'app-rename-user',
  standalone: true,
  imports: [
    AsyncPipe,
    NgIf
  ],
  templateUrl: './rename-user.component.html',
  styleUrl: './rename-user.component.css'
})
export class RenameUserComponent extends Identifiable{
  protected user$: Observable<User>;

  constructor(private userService: UserServiceAbstract) {
    super()
    this.user$ = this.userService.getUser();
  }

  updateName(name: string) {
    this.userService.renameUser(name)
  }
}
