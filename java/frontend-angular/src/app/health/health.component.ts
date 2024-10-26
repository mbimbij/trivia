import {Component} from '@angular/core';
import {Identifiable} from "../shared/identifiable";

@Component({
  selector: 'app-health',
  standalone: true,
  imports: [],
  templateUrl: './health.component.html',
  styleUrl: './health.component.css'
})
export class HealthComponent extends Identifiable{

}
