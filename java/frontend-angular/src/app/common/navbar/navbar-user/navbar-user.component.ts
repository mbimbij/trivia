import {ChangeDetectionStrategy, Component, ElementRef, TemplateRef, ViewChild, ViewContainerRef} from '@angular/core';
import {AsyncPipe, NgIf} from "@angular/common";
import {LogoutButtonComponent} from "./logout-button/logout-button.component";
import {MatIcon} from "@angular/material/icon";
import {Identifiable} from "../../identifiable";
import {Observable} from "rxjs";
import {User} from "../../../user/user";
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
export class NavbarUserComponent extends Identifiable{
  user$: Observable<User>;

  constructor(private userService: UserServiceAbstract,
              protected authenticationService: AuthenticationServiceAbstract,
              private viewContainerRef: ViewContainerRef,
              private hostElement: ElementRef) {
    super()
    this.user$ = this.userService.getUser();
    console.log(this.hostElement.nativeElement.outerHTML);
  }

  override checkRender(): string {
    return super.checkRender();
  }
}
