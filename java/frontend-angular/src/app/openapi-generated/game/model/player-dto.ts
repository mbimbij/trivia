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
import {State} from './state';


export interface PlayerDto {
    id: string;
    name: string;
    coinCount: number;
    state: State;
    location: number;
    gotOutOfPenaltyBox: boolean;
}
export namespace PlayerDto {
}


