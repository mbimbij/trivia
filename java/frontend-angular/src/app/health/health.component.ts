import {Component} from '@angular/core';
import {IdentifiableImpl} from "../shared/identifiableImpl";

@Component({
  selector: 'app-health',
  standalone: true,
  imports: [],
  templateUrl: './health.component.html',
  styleUrl: './health.component.css'
})
export class HealthComponent extends IdentifiableImpl{

}
