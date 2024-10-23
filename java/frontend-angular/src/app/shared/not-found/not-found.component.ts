import {ChangeDetectionStrategy, Component} from '@angular/core';
import {Identifiable} from "../identifiable";

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [],
  template: `
    Page not found
    {{ checkRender() }}
  `,
  styleUrl: './not-found.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class NotFoundComponent extends Identifiable {

}
