import {ChangeDetectionStrategy, ChangeDetectorRef, Component} from '@angular/core';
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {Identifiable} from "../../common/identifiable";
import {AsyncPipe, NgIf} from "@angular/common";
import {HelloDirectiveDirective} from "../../hello-directive.directive";

@Component({
  selector: 'app-rename-user',
  standalone: true,
  imports: [
    AsyncPipe,
    NgIf,
    HelloDirectiveDirective
  ],
  templateUrl: './rename-user.component.html',
  styleUrl: './rename-user.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class RenameUserComponent extends Identifiable{

  constructor(protected userService: UserServiceAbstract) {
    super()
  }

  override checkRender(): string {
    return super.checkRender();
  }
}
