import {ResolveFn} from '@angular/router';
import {UserServiceAbstract} from "./services/user-service.abstract";
import {inject} from "@angular/core";
import {GameServiceAbstract} from "./services/game-service-abstract";

export const gameListResolver: ResolveFn<any> = (route, state) => {
  let userService = inject(UserServiceAbstract);
  let gameService = inject(GameServiceAbstract);
  return {
    user$: userService.getUser(),
    games$: gameService.getGames()
  };
};
