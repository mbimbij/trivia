import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {Game} from "../game/game";
import {Observable, ReplaySubject} from "rxjs";

export class GameListDataSource extends DataSource<Game> {
  private _dataStream = new ReplaySubject<Game[]>();

  constructor() {
    super()
  }

  override connect(collectionViewer: CollectionViewer): Observable<readonly Game[]> {
    return this._dataStream;
  }

  override disconnect(collectionViewer: CollectionViewer): void {
  }

  public setData(games: Game[]) {
    this._dataStream.next(games)
  }
}
