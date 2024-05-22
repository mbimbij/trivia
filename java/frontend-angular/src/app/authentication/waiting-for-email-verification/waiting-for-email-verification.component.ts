import {Component} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-waiting-for-email-verification',
  standalone: true,
  imports: [],
  templateUrl: './waiting-for-email-verification.component.html',
  styleUrl: './waiting-for-email-verification.component.css'
})
export class WaitingForEmailVerificationComponent {

  constructor(protected router: Router) {
  }
}
