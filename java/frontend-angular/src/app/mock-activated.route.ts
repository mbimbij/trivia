import {Observable, of} from "rxjs";

export class MockActivatedRoute {
  params: Observable<any> = of({id: '123'}); // Mock route params
}
