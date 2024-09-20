import {Injectable} from '@angular/core';
import {sprintf} from "sprintf-js";
import {Player} from "../user/player";
import {Game} from "../game/game";

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  constructor() {
  }

  private messages: { [key: string]: string } = {
    ['roll-dice.results.inside-penalty-box.even']: 'You rolled an even number: %d. You are getting out of the penalty box',
    ['roll-dice.results.inside-penalty-box.odd']: 'You rolled an odd number: %d. You stay in the penalty box',
    ['roll-dice.results.outside-penalty-box.after-escape']: 'Your new location is %d. The category is %s',
    ['roll-dice.results.outside-penalty-box']: 'You rolled a %d. Your new location is %d. The category is %s',
    ['answer-question.results.correct']: 'Correct Answer',
    ['answer-question.results.first-incorrect']: 'First Incorrect Answer. You are given a second chance',
    ['answer-question.results.second-incorrect']: 'Second Incorrect Answer. You are sent to the penalty box.',
    ['go-back-button']: 'back to games',
  };

  getMessage(code: string): string {
    return this.messages[code];
  }

  getRollDiceResultsInsidePenaltyBoxMessage(game: Game, player: Player) {
    if (!player.isWaitingToValidateRollDiceInsidePenaltyBox()) {
      return undefined
    }

    let roll = game.currentRoll!;

    if (roll % 2 == 0) {
      return sprintf(this.messages['roll-dice.results.inside-penalty-box.even'], roll)
    } else {
      return sprintf(this.messages['roll-dice.results.inside-penalty-box.odd'], roll)
    }
  }

  getRollDiceResultsOutsidePenaltyBoxMessage(game: Game, player:Player) {
    if (!player.isWaitingToValidateRollDiceOutsidePenaltyBox()) {
      return undefined
    }

    if(player.gotOutOfPenaltyBox){
      return sprintf(this.messages['roll-dice.results.outside-penalty-box.after-escape'],
        player.location,
        game.currentCategory)
    }else{
      return sprintf(this.messages['roll-dice.results.outside-penalty-box'],
        game.currentRoll,
        player.location,
        game.currentCategory)
    }
  }
}

export enum MessageCode{

}
