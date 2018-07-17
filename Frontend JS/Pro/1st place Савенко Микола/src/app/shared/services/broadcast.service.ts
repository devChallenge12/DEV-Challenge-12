import {Subject, Observable} from 'rxjs';
import {filter, map} from 'rxjs/operators';

interface BroadcastEvent {
  key: any;
  data?: any;
}

/**
 * Broadcast service - event bus service
 */
export class BroadcastService {
  private _eventBus: Subject<BroadcastEvent>;

  constructor() {
    this._eventBus = new Subject<BroadcastEvent>();
  }

  /**
   * Send data to the event
   * @param key
   * @param data
   */
  broadcast(key: any, data?: any) {
    this._eventBus.next({key, data});
  }

  /**
   * Subscribe on event by key
   * @param key
   * @returns {Observable<T>}
   */
  on<T>(key: any): Observable<T> {
    return this._eventBus.asObservable()
      .pipe(
        filter(event => event.key === key),
        map(event => <T>event.data)
      );
  }
}

export const Broadcaster = new BroadcastService();
