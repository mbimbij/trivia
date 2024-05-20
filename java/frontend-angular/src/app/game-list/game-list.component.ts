import {Component} from '@angular/core';
import {CreateGameComponent} from "../create-game/create-game.component";
import {FormsModule} from "@angular/forms";
import {NgForOf, NgIf} from "@angular/common";
import {GameResponseDto, UserDto} from "../openapi-generated";
import {GameServiceAbstract} from "../game-service-abstract";
import {Router, RouterLink} from "@angular/router";
import {ObjectAttributePipe} from "../object-attribute.pipe";
import {JoinGameButtonComponent} from "../join-game-button/join-game-button.component";
import {UserService} from "../user.service";
import {GotoGameButtonComponent} from "../goto-game-button/goto-game-button.component";
import {StartGameButtonComponent} from "../start-game-button/start-game-button.component";
import {DeleteGameButtonComponent} from "../delete-game-button/delete-game-button.component";
import {FirebaseuiAngularLibraryComponent} from "firebaseui-angular";
import {AuthenticationService} from "../authentication.service";
import {Observable} from "rxjs";
import {NavbarComponent} from "../navbar/navbar.component";
import {User} from "../user";

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
    NavbarComponent
  ],
  templateUrl: './game-list.component.html',
  styleUrl: './game-list.component.css'
})
export class GameListComponent {
  games: GameResponseDto[] = [];
  user!: User;

  constructor(private gameService: GameServiceAbstract,
              private userService: UserService) {
  }

  ngOnInit(): void {
    this.user = this.userService.getUser();
    this.gameService.getGames()
      .subscribe(games => {
        this.games = games;
        this.games.forEach(game => {
          this.gameService.registerGameUpdatedObserver(game.id, this.updateGameWithArrow);
        })
      });
    this.gameService.registerGameCreatedObserver(this.addGameArrow)
    this.gameService.registerGameDeletedObserver(this.deleteGameArrow)
  }

  addGame(newGame: GameResponseDto) {
    this.gameService.registerGameUpdatedObserver(newGame.id, this.updateGameWithArrow);
    this.games.push(newGame);
  }

  protected addGameArrow = (newGame: GameResponseDto) => {
    this.addGame(newGame);
  }

  protected deleteGameArrow = (gameId: number) => {
    const index = this.games.findIndex(
      game => game.id === gameId);
    if (index !== -1) {
      this.games.splice(index, 1);
    }
  }

  protected updateGameWith(replacement: GameResponseDto) {
    const index = this.games.findIndex(
      game => game.id === replacement.id);
    if (index !== -1) {
      this.games.splice(index, 1, replacement);
    }
  }

  protected updateGameWithArrow = (replacement: GameResponseDto) => {
    this.updateGameWith(replacement);
  }

  syncPlayerToLocalStorage() {
    this.userService.setUser(this.user)
  }
}
