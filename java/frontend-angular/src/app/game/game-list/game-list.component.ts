import {ChangeDetectionStrategy, Component} from '@angular/core';
import {CreateGameComponent} from "../create-game/create-game.component";
import {FormsModule} from "@angular/forms";
import {AsyncPipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {RouterLink} from "@angular/router";
import {ObjectAttributePipe} from "../../common/object-attribute.pipe";
import {FirebaseuiAngularLibraryComponent} from "firebaseui-angular";
import {JoinGameButtonComponent} from "../join-game-button/join-game-button.component";
import {GotoGameButtonComponent} from "../goto-game-button/goto-game-button.component";
import {StartGameButtonComponent} from "../start-game-button/start-game-button.component";
import {DeleteGameButtonComponent} from "../delete-game-button/delete-game-button.component";
import {NavbarComponent} from "../../common/navbar/navbar.component";
import {Game} from "../game";
import {Identifiable} from "../../common/identifiable";
import {RenameUserComponent} from "../../user/rename-user/rename-user.component";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {JoinGameButton2Component} from "../join-game-button-2/join-game-button-2.component";

@Component({
  selector: 'app-game-list',
  standalone: true,
  imports: [
    CreateGameComponent,
    FormsModule,
    NgForOf,
    NgIf,
    ObjectAttributePipe,
    RouterLink,
    JoinGameButtonComponent,
    GotoGameButtonComponent,
    StartGameButtonComponent,
    DeleteGameButtonComponent,
    FirebaseuiAngularLibraryComponent,
    NavbarComponent,
    AsyncPipe,
    NgClass,
    RenameUserComponent,
    JoinGameButton2Component,
  ],
  templateUrl: './game-list.component.html',
  styleUrl: './game-list.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GameListComponent extends Identifiable {

  constructor(
    protected gameService: GameServiceAbstract,
    protected userService: UserServiceAbstract
  ) {
    super()
  }

  trackByFn(index: number, game: Game): number {
    return game.id; // Assuming each game has a unique ID
  }

  override checkRender(): string {
    return super.checkRender();
  }
}
