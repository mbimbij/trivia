import { Component } from '@angular/core';
import {Identifiable} from "../identifiable";

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [],
  template: `
    Page not found
  `,
  styleUrl: './not-found.component.css'
})
export class NotFoundComponent extends Identifiable {

}
