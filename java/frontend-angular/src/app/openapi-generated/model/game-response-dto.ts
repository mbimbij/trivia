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
import { UserDto } from './user-dto';


export interface GameResponseDto { 
    id: number;
    name: string;
    state: string;
    turn: number;
    creator: UserDto;
    players: Array<UserDto>;
    currentPlayer: UserDto;
}
