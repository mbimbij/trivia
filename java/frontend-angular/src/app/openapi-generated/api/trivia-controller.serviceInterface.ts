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
import { HttpHeaders }                                       from '@angular/common/http';

import { Observable }                                        from 'rxjs';

import { AnswerCode } from '../model/models';
import { CreateGameRequestDto } from '../model/models';
import { GameLog } from '../model/models';
import { GameResponseDto } from '../model/models';
import { UserDto } from '../model/models';


import { Configuration }                                     from '../configuration';



export interface TriviaControllerServiceInterface {
    defaultHeaders: HttpHeaders;
    configuration: Configuration;

    /**
     * 
     * 
     * @param gameId 
     * @param playerId 
     * @param userDto 
     */
    addPlayerToGame(gameId: number, playerId: string, userDto: UserDto, extraHttpRequestParams?: any): Observable<GameResponseDto>;

    /**
     * 
     * 
     * @param gameId 
     * @param playerId 
     * @param answerCode 
     */
    answer(gameId: number, playerId: string, answerCode: AnswerCode, extraHttpRequestParams?: any): Observable<boolean>;

    /**
     * 
     * 
     * @param createGameRequestDto 
     */
    createGame(createGameRequestDto: CreateGameRequestDto, extraHttpRequestParams?: any): Observable<GameResponseDto>;

    /**
     * 
     * 
     * @param gameId 
     */
    deleteGameById(gameId: number, extraHttpRequestParams?: any): Observable<{}>;

    /**
     * 
     * 
     * @param gameId 
     * @param playerId 
     */
    drawQuestion(gameId: number, playerId: string, extraHttpRequestParams?: any): Observable<GameResponseDto>;

    /**
     * get a game by its id
     * 
     * @param gameId 
     */
    getGameById(gameId: number, extraHttpRequestParams?: any): Observable<GameResponseDto>;

    /**
     * 
     * 
     * @param gameId 
     */
    getGameLogs(gameId: number, extraHttpRequestParams?: any): Observable<Array<GameLog>>;

    /**
     * 
     * 
     */
    listGames(extraHttpRequestParams?: any): Observable<Array<GameResponseDto>>;

    /**
     * 
     * 
     * @param gameId 
     * @param playerId 
     */
    rollDice(gameId: number, playerId: string, extraHttpRequestParams?: any): Observable<GameResponseDto>;

    /**
     * 
     * 
     * @param gameId 
     * @param playerId 
     */
    startGame(gameId: number, playerId: string, extraHttpRequestParams?: any): Observable<GameResponseDto>;

}
