import {Component, Input} from '@angular/core';
import {AsyncPipe, NgIf} from "@angular/common";
import {Subject} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";

@Component({
  selector: 'app-error-display',
  standalone: true,
  imports: [
    AsyncPipe,
    NgIf
  ],
  templateUrl: './error-display.component.html',
  styleUrl: './error-display.component.css'
})
export class ErrorDisplayComponent {
  @Input() gameId!: number;
  @Input() gameLoadingError$= new Subject<HttpErrorResponse>();
}
