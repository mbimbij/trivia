import {ChangeDetectionStrategy, Component, Input} from '@angular/core';
import {AsyncPipe, NgIf} from "@angular/common";
import {Subject} from "rxjs";
import {HttpErrorResponse} from "@angular/common/http";
import {Identifiable} from "../../common/identifiable";

@Component({
  selector: 'app-error-display',
  standalone: true,
  imports: [
    AsyncPipe,
    NgIf
  ],
  templateUrl: './error-display.component.html',
  styleUrl: './error-display.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ErrorDisplayComponent extends Identifiable {
  @Input() gameId!: number;
  @Input() gameLoadingError$= new Subject<HttpErrorResponse>();
}
