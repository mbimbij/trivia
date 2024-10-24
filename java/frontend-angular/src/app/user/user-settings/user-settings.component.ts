import {Component, Input} from '@angular/core';
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {Identifiable} from "../../shared/identifiable";
import {AsyncPipe, NgIf} from "@angular/common";

@Component({
  selector: 'app-user-settings',
  standalone: true,
  imports: [
    NgIf,
    AsyncPipe
  ],
  templateUrl: './user-settings.component.html',
  styleUrl: './user-settings.component.css'
})
export class UserSettingsComponent extends Identifiable{
  @Input() userName!: string;

  constructor(protected userService: UserServiceAbstract) {
    super()
  }
}
