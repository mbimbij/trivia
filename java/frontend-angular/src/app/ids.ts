const reset = {RESET: 'reset'};
const submit = {SUBMIT: 'submit'};
const validate = {VALIDATE: 'validate'};

const commonDialogElements = {
  CANCEL: 'cancel',
  BACKEND_ERROR_MESSAGE: 'backend-error-message',
  ...reset,
  ...validate
}
export const ids = {
  createGame: {
    OPEN_DIALOG_BUTTON: 'create-game',
    DIALOG: 'create-game-dialog',
    GAME_NAME: 'game-name',
    CREATOR_NAME: 'creator-name',
    ...commonDialogElements
  },
  joinGame: {
    openDialogButtonForGameId: function (gameId: number) {
      return 'join-button-' + gameId;
    },
    DIALOG: 'join-game-dialog',
    PLAYER_NAME: 'player-name',
    PLAYER_NAME_LABEL: 'player-name-label',
    ...commonDialogElements
  },
  userSettings: {
    renameUser: 'userName',
    ...submit,
    ...reset
  }
}
