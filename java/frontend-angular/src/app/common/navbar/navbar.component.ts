import {AfterViewChecked, ChangeDetectionStrategy, ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {MatToolbar, MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {MatButtonModule} from "@angular/material/button";
import {AsyncPipe, KeyValuePipe, NgIf} from "@angular/common";
import {ObjectAttributePipe} from "../object-attribute.pipe";
import {Router} from "@angular/router";
import {Identifiable} from "../identifiable";
import {NavbarUserComponent} from "./navbar-user/navbar-user.component";

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [
    MatToolbar,
    NavbarUserComponent
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})

export class NavbarComponent extends Identifiable {
  constructor(protected router: Router) {
    super()
  }

  override checkRender(): string {
    return super.checkRender();
  }
}
