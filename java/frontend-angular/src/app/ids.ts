export const ids = {
  createGame: {
    OPEN_DIALOG_BUTTON: 'create-game',
    DIALOG: 'create-game-dialog',
    GAME_NAME: 'game-name',
    CREATOR_NAME: 'creator-name',
    CANCEL: 'cancel',
    RESET: 'reset',
    VALIDATE: 'validate',
  },
  joinGame:{
    openDialogButtonForGameId: function (gameId: number){
      return 'join-game-'+gameId;
    },
    DIALOG: 'join-game-dialog',
    PLAYER_NAME: 'player-name',
    PLAYER_NAME_LABEL: 'player-name-label',
    CANCEL: 'cancel',
    RESET: 'reset',
    VALIDATE: 'validate',
  }
}
