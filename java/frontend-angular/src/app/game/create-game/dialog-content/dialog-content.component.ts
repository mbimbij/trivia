import {ChangeDetectionStrategy, Component, Input, OnDestroy, OnInit} from '@angular/core';
import {
  MatDialogActions,
  MatDialogClose,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";
import {MatButton} from "@angular/material/button";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {GameServiceAbstract} from "../../../services/game-service-abstract";
import {Identifiable} from "../../../common/identifiable";
import {User} from "../../../user/user";
import {UserDto} from "../../../openapi-generated/game";
import {MatIcon} from "@angular/material/icon";
import {FormsModule} from "@angular/forms";
import {CreateGameComponentTestIds} from "../create-game.component";

@Component({
  selector: 'app-dialog-content',
  standalone: true,
  imports: [
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    MatButton,
    MatDialogClose,
    MatFormField,
    MatInput,
    MatLabel,
    MatIcon,
    FormsModule
  ],
  templateUrl: './dialog-content.component.html',
  styleUrl: './dialog-content.component.css',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DialogContentComponent extends Identifiable implements OnInit, OnDestroy{

  @Input() user!: User
  newGameName!: string;
  creatorName!: string;

  constructor(private matDialogRef: MatDialogRef<DialogContentComponent>,
              private gameService: GameServiceAbstract) {
    super()
  }

  ngOnInit() {
    console.log(`ngOnInit ${this.id}`)
    this.creatorName = this.user.name
  }

  ngOnDestroy() {
    console.log(`ngOnDestroy ${this.id}`)
  }

  protected createGame(newGameName: string, creatorName: string) {
    console.log(`create game ${newGameName} with name ${creatorName}`)
    let creator = {name: creatorName, id: this.user.id} as UserDto
    this.gameService.create(newGameName, creator).subscribe({
      next: newGame => {
        console.log(`created game: ${newGame.id}`)
        this.matDialogRef.close()
      }
    })
  }

  protected readonly CreateGameComponentTestIds = CreateGameComponentTestIds;
}
