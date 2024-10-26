import {Component} from '@angular/core';
import {Router} from "@angular/router";
import {AuthenticationServiceAbstract} from "../../services/authentication-service-abstract";
import {AsyncPipe, NgIf} from "@angular/common";
import {Identifiable} from "../../shared/identifiable";

@Component({
  selector: 'app-waiting-for-email-verification',
  standalone: true,
  imports: [
    NgIf,
    AsyncPipe
  ],
  templateUrl: './waiting-for-email-verification.component.html',
  styleUrl: './waiting-for-email-verification.component.css'
})
export class WaitingForEmailVerificationComponent extends Identifiable{

  constructor(protected router: Router,
              protected authenticationService:AuthenticationServiceAbstract) {
    super()
  }
}
