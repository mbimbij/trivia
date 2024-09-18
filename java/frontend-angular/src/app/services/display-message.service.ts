import {Injectable} from '@angular/core';
import {sprintf} from "sprintf-js";
import {Player} from "../user/player";
import {Game} from "../game/game";

@Injectable({
  providedIn: 'root'
})
export class DisplayMessageService {

  constructor() {
  }

  private messages: { [key: string]: string } = {
    ['roll-dice.results.inside-penalty-box.even']: 'You rolled an even number: %d. You are getting out of the penalty box',
    ['roll-dice.results.inside-penalty-box.odd']: 'You rolled an odd number: %d. You stay in the penalty box',
    ['roll-dice.results.outside-penalty-box.after-escape']: 'Your new location is %d. The category is %s',
    ['roll-dice.results.outside-penalty-box']: 'You rolled a %d. Your new location is %d. The category is %s',
  };

  getMessage(code: string): string {
    return this.messages[code];
  }

  getRollDiceResultsInsidePenaltyBoxMessage(roll: number | undefined) {
    if (roll == undefined) {
      return undefined
    }

    if (roll % 2 == 0) {
      return sprintf(this.messages['roll-dice.results.inside-penalty-box.even'], roll)
    } else {
      return sprintf(this.messages['roll-dice.results.inside-penalty-box.odd'], roll)
    }
  }

  getRollDiceResultsOutsidePenaltyBoxMessage(game: Game, player:Player) {
    if (game.currentRoll == undefined) {
      return undefined
    }

    if(player.gotOutOfPenaltyBox){
      return sprintf(this.messages['roll-dice.results.outside-penalty-box.after-escape'], player.location, game.currentCategory)
    }else{
      return sprintf(this.messages['roll-dice.results.outside-penalty-box'], game.currentRoll, player.location, game.currentCategory)
    }
  }
}
