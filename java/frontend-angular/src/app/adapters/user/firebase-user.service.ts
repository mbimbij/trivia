import {Injectable, OnDestroy} from '@angular/core';
import {BehaviorSubject, map, Observable, Subscription} from "rxjs";
import {Nobody, User} from "../../user/user";
import {AngularFireAuth} from "@angular/fire/compat/auth";
import {UserServiceAbstract} from "../../services/user-service.abstract";

@Injectable({
  providedIn: 'root'
})
export class FirebaseUserService extends UserServiceAbstract implements OnDestroy{
  userSubject = new BehaviorSubject<User>(Nobody.instance);
  private subscription1?: Subscription;
  private subscription2?: Subscription;

  constructor(private afAuth: AngularFireAuth) {
    super();
    this.updateUserSubject();
  }

  ngOnDestroy(): void {
        this.subscription1?.unsubscribe();
        this.subscription2?.unsubscribe();
    }

  override getUser(): Observable<User> {
    return this.userSubject.asObservable()
  }

  override renameUser(newUserName: string): void {
    if(this.subscription1){
      this.subscription1.unsubscribe()
    }
    this.subscription1 = this.afAuth.user
      .subscribe(user => {
        user?.updateProfile({displayName: newUserName})
          .then(() => {
            this.updateUserSubject()
          })
      });
  }

  private updateUserSubject() {
    if(this.subscription2){
      this.subscription2.unsubscribe()
    }
    this.subscription2 = this.afAuth.user.pipe(map(user => this.buildDomainUser(user))).subscribe(
      value => {
        this.userSubject.next(value);
      }
    );
  }

  private buildDomainUser(user: firebase.default.User | null) {
    return user ? new User(user.uid, user.displayName!, user.isAnonymous) : Nobody.instance;
  }
}

