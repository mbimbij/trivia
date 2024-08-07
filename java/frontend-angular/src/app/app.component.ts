import {Component} from '@angular/core';
import {Identifiable} from "./common/identifiable";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent extends Identifiable {
  title: string = 'frontend-angular';
}
