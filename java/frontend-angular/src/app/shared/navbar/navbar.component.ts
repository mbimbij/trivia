import {ChangeDetectionStrategy, Component} from '@angular/core';
import {MatToolbar} from "@angular/material/toolbar";
import {Router} from "@angular/router";
import {IdentifiableImpl} from "../identifiableImpl";
import {NavbarUserComponent} from "./navbar-user/navbar-user.component";
import {MatButton} from "@angular/material/button";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    MatToolbar,
    NavbarUserComponent,
    MatButton
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class NavbarComponent extends IdentifiableImpl {
  constructor(protected router: Router) {
    super()
  }

  override checkRender(): string {
    return super.checkRender();
  }
}
