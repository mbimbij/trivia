import {ChangeDetectionStrategy, Component} from '@angular/core';
import {IdentifiableImpl} from "./shared/identifiableImpl";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AppComponent extends IdentifiableImpl {
  title: string = 'frontend-angular';

  override checkRender(): string {
    return super.checkRender();
  }
}
