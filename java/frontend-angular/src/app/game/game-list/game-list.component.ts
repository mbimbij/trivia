import {ChangeDetectionStrategy, Component} from '@angular/core';
import {OpenCreateGameComponent} from "../create-game/open-create-game.component";
import {FormsModule} from "@angular/forms";
import {AsyncPipe, NgClass, NgForOf, NgIf} from "@angular/common";
import {GameServiceAbstract} from "../../services/game-service-abstract";
import {RouterLink} from "@angular/router";
import {FirebaseuiAngularLibraryComponent} from "firebaseui-angular";
import {GotoGameButtonComponent} from "../goto-game-button/goto-game-button.component";
import {StartGameButtonComponent} from "../start-game-button/start-game-button.component";
import {DeleteGameButtonComponent} from "../delete-game-button/delete-game-button.component";
import {RenameUserComponent} from "../../user/rename-user/rename-user.component";
import {UserServiceAbstract} from "../../services/user-service.abstract";
import {OpenJoinGameDialogComponent} from "../join-game-button/open-join-game-dialog.component";
import {
  MatCell,
  MatCellDef,
  MatColumnDef,
  MatHeaderCell,
  MatHeaderCellDef,
  MatHeaderRow,
  MatHeaderRowDef,
  MatRow,
  MatRowDef,
  MatTable
} from "@angular/material/table";
import {GameListDataSource} from "../../shared/game-list-data.source";
import {ObjectAttributePipe} from "../../shared/object-attribute.pipe";
import {NavbarComponent} from "../../shared/navbar/navbar.component";
import {Identifiable} from "../../shared/identifiable";

@Component({
  selector: 'app-game-list',
  standalone: true,
  imports: [
    OpenCreateGameComponent,
    FormsModule,
    NgForOf,
    NgIf,
    ObjectAttributePipe,
    RouterLink,
    OpenJoinGameDialogComponent,
    GotoGameButtonComponent,
    StartGameButtonComponent,
    DeleteGameButtonComponent,
    FirebaseuiAngularLibraryComponent,
    NavbarComponent,
    AsyncPipe,
    NgClass,
    RenameUserComponent,
    OpenJoinGameDialogComponent,
    MatTable,
    MatColumnDef,
    MatHeaderCell,
    MatCell,
    MatHeaderCellDef,
    MatCellDef,
    MatHeaderRow,
    MatRow,
    MatHeaderRowDef,
    MatRowDef,
  ],
  templateUrl: './game-list.component.html',
  styleUrl: './game-list.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class GameListComponent extends Identifiable {

  protected dataSource = new GameListDataSource();
  protected displayedColumns = ['id', 'name', 'creator', 'players', 'state', 'join', 'start', 'goto', 'delete']

  constructor(
    protected gameService: GameServiceAbstract,
    protected userService: UserServiceAbstract
  ) {
    super()
  }

  ngOnInit(){
    this.gameService.getGames().subscribe(games => {
      this.dataSource.setData(games)
    })
  }
}
