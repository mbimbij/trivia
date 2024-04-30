import {Component, EventEmitter, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {GameResponseDto, TriviaControllerService} from "../openapi-generated";
import {FormsModule} from "@angular/forms";
import {tap} from "rxjs";

@Component({
  selector: 'app-create-game',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './create-game.component.html',
  styleUrl: './create-game.component.css'
})
export class CreateGameComponent implements OnChanges{
  @Input() playerName: string = '';

  @Output() newGameEvent = new EventEmitter<GameResponseDto>();

  constructor(private service: TriviaControllerService) {
  }

  createGame(newGameName: string) {
    console.log(`player: ${this.playerName}. creating new game with name: ${newGameName}`)
    this.service.createGame({gameName: newGameName, creator: {id: 1, name: this.playerName}})
      .pipe(
        tap(response => console.log(`created game "${newGameName}"`)),
      )
      .subscribe(
        {
          next: (newGame) => {
            console.log(newGame)
            console.log("emitting event: " + JSON.stringify(newGame))
            this.newGameEvent.emit(newGame);
          },
          // error:  (_) => this.handleError(`createItem(${this.newItemName})`, [])
        }
      )
  }

  ngOnChanges(changes: SimpleChanges): void {
    if(changes['playerName']){
      let newName = changes['playerName'].currentValue;
      this.playerName = newName;
    }
  }
}
