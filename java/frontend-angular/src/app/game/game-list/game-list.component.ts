import {ChangeDetectionStrategy, Component} from '@angular/core';
import {CreateGameComponent} from "../create-game/create-game.component";
import {FormsModule} from "@angular/forms";
import {AsyncPipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {RouterLink} from "@angular/router";
import {ObjectAttributePipe} from "../../common/object-attribute.pipe";
import {FirebaseuiAngularLibraryComponent} from "firebaseui-angular";
import {User} from "../../user/user";
import {JoinGameButtonComponent} from "../join-game-button/join-game-button.component";
import {GotoGameButtonComponent} from "../goto-game-button/goto-game-button.component";
import {StartGameButtonComponent} from "../start-game-button/start-game-button.component";
import {DeleteGameButtonComponent} from "../delete-game-button/delete-game-button.component";
import {NavbarComponent} from "../navbar/navbar.component";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {Observable} from "rxjs";
import {Game} from "../game";

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
    NgClass
  ],
  templateUrl: './game-list.component.html',
  styleUrl: './game-list.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GameListComponent {
  user$!: Observable<User>;
  games$!: Observable<Array<Game>>

  constructor(
    protected gameService: GameServiceAbstract,
    protected userService: UserServiceAbstract) {
    console.log(`constructor ${this.constructor.name} called`)
  }

  updateName(name: string) {
    this.userService.renameUser(name)
  }

  ngOnInit(): void {
    console.log(`ngOnInit ${this.constructor.name} called`)
    this.user$ = this.userService.getUser();
    this.games$ = this.gameService.getGames();
  }

  trackByFn(index: number, game: Game): number {
    return game.id; // Assuming each game has a unique ID
  }
}
