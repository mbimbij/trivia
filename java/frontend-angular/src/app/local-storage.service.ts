import {Injectable} from '@angular/core';
import {Subject} from "rxjs";
import {UserDto} from "./openapi-generated";

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {
  private userSubject = new Subject<UserDto>();

  constructor() {
  }
  updatePlayer(value: UserDto): void {
    localStorage.setItem('user', JSON.stringify(value));
    this.userSubject.next(value)
  }
  registerUserUpdatedObserver(observer: (newPlayerName: UserDto) => void) {
    this.userSubject.subscribe(observer)
  }

  getUser(): UserDto{
    return JSON.parse(localStorage.getItem('user')!)
  }
}
