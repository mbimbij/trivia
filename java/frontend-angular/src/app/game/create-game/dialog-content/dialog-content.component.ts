import {Component, Input} from '@angular/core';
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
    MatLabel
  ],
  templateUrl: './dialog-content.component.html',
  styleUrl: './dialog-content.component.css'
})
export class DialogContentComponent extends Identifiable{

  @Input() user!: User

  constructor(private matDialogRef: MatDialogRef<DialogContentComponent>,
              private gameService:GameServiceAbstract) {
    super()
  }

  protected createGame(newGameName: string){
    console.log(`create game ${newGameName} with name ${this.user.name}`)
    this.gameService.create(newGameName, this.user).subscribe({
      next: newGame => {
        console.log(`created game: ${newGame.id}`)
        this.matDialogRef.close()
      }
    })
  }
}
