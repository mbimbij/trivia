import {Injectable} from '@angular/core';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class LocalStorageService {

  private subject = new Subject<string>();

  constructor() {
  }

  updatePlayerName(value: string): void {
    localStorage.setItem('playerName', value);
    this.subject.next(value)
  }

  registerObserver(observer: (newPlayerName: string) => void) {
    this.subject.subscribe(observer)
  }
}
