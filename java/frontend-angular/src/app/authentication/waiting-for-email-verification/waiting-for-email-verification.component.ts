import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationServiceAbstract} from "../../services/authentication-service.abstract";
import {NgIf} from "@angular/common";

@Component({
  selector: 'app-waiting-for-email-verification',
  standalone: true,
  imports: [
    NgIf
  ],
  templateUrl: './waiting-for-email-verification.component.html',
  styleUrl: './waiting-for-email-verification.component.css'
})
export class WaitingForEmailVerificationComponent {
  constructor(protected router: Router,
              protected authenticationService:AuthenticationServiceAbstract) {
  }
}
