/**
 * OpenAPI definition
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: v0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


export type State = 'CREATED' | 'STARTED' | 'ENDED' | 'WAITING_FOR_DICE_ROLL' | 'WAITING_FOR_ROLL_DICE_EVALUATION' | 'IN_PENALTY_BOX' | 'WAITING_TO_UPDATE_LOCATION' | 'WAITING_TO_DRAW_1ST_QUESTION' | 'WAITING_FOR_1ST_ANSWER' | 'WAITING_FOR_1ST_ANSWER_EVALUATION' | 'WAITING_TO_DRAW_2ND_QUESTION' | 'WAITING_FOR_2ND_ANSWER' | 'WAITING_FOR_2ND_ANSWER_EVALUATION' | 'WAITING_TO_END_TURN_OR_GAME' | 'WAITING_TO_VALIDATE_FIRST_CORRECT_ANSWER' | 'WAITING_TO_VALIDATE_FIRST_INCORRECT_ANSWER' | 'WAITING_TO_VALIDATE_SECOND_CORRECT_ANSWER' | 'WAITING_TO_VALIDATE_SECOND_INCORRECT_ANSWER' | 'WAITING_TO_VALIDATE_ODD_DICE_ROLL_FROM_PENALTY_BOX' | 'WAITING_TO_VALIDATE_EVEN_DICE_ROLL_FROM_PENALTY_BOX' | 'GAME_END';

export const State = {
    Created: 'CREATED' as State,
    Started: 'STARTED' as State,
    Ended: 'ENDED' as State,
    WaitingForDiceRoll: 'WAITING_FOR_DICE_ROLL' as State,
    WaitingForRollDiceEvaluation: 'WAITING_FOR_ROLL_DICE_EVALUATION' as State,
    InPenaltyBox: 'IN_PENALTY_BOX' as State,
    WaitingToUpdateLocation: 'WAITING_TO_UPDATE_LOCATION' as State,
    WaitingToDraw1StQuestion: 'WAITING_TO_DRAW_1ST_QUESTION' as State,
    WaitingFor1StAnswer: 'WAITING_FOR_1ST_ANSWER' as State,
    WaitingFor1StAnswerEvaluation: 'WAITING_FOR_1ST_ANSWER_EVALUATION' as State,
    WaitingToDraw2NdQuestion: 'WAITING_TO_DRAW_2ND_QUESTION' as State,
    WaitingFor2NdAnswer: 'WAITING_FOR_2ND_ANSWER' as State,
    WaitingFor2NdAnswerEvaluation: 'WAITING_FOR_2ND_ANSWER_EVALUATION' as State,
    WaitingToEndTurnOrGame: 'WAITING_TO_END_TURN_OR_GAME' as State,
    WaitingToValidateFirstCorrectAnswer: 'WAITING_TO_VALIDATE_FIRST_CORRECT_ANSWER' as State,
    WaitingToValidateFirstIncorrectAnswer: 'WAITING_TO_VALIDATE_FIRST_INCORRECT_ANSWER' as State,
    WaitingToValidateSecondCorrectAnswer: 'WAITING_TO_VALIDATE_SECOND_CORRECT_ANSWER' as State,
    WaitingToValidateSecondIncorrectAnswer: 'WAITING_TO_VALIDATE_SECOND_INCORRECT_ANSWER' as State,
    WaitingToValidateOddDiceRollFromPenaltyBox: 'WAITING_TO_VALIDATE_ODD_DICE_ROLL_FROM_PENALTY_BOX' as State,
    WaitingToValidateEvenDiceRollFromPenaltyBox: 'WAITING_TO_VALIDATE_EVEN_DICE_ROLL_FROM_PENALTY_BOX' as State,
    GameEnd: 'GAME_END' as State
};

