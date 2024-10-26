import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {IdentifiableImpl} from "../identifiableImpl";
import {MessageService} from "../../services/message.service";

@Component({
  selector: 'app-go-back-button',
  standalone: true,
  imports: [],
  template: `
    <button style="display: block"
            [attr.data-testid]="'go-back'"
            (click)="router.navigate(['/games'])">
      {{ buttonText }}
    </button>
  `,
  styleUrl: './go-back-button.component.css'
})
export class GoBackButtonComponent extends IdentifiableImpl implements OnInit {
  protected buttonText!: string;

  constructor(protected router: Router,
              private messageService: MessageService) {
    super()
  }

  ngOnInit(): void {
    this.buttonText=this.messageService.getMessage('go-back-button')
  }
}
