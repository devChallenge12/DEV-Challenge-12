/**
 * @name StorageAdapter
 * @description Store Adapter for working with localStorage
 */
import {forkJoin, Observable, of, throwError} from 'rxjs';
import {map, mergeMap} from 'rxjs/internal/operators';

export enum STORAGE_KEY {
  LIBRARIES = 'LIBRARY:DB:LIBRARIES',
  BOOKS = 'LIBRARY:DB:BOOKS',
  TOKEN = 'LIBRARY:DB:TOKEN',
}

interface IId {
  id: string;
}

/**
 * The storage service-adapter closely works with LocalStorage
 */
export class StorageAdapter {

  /**
   * Initialize two local storage rows LIBRARIES and BOOKS
   */
  static init() {
    forkJoin(
      StorageAdapter.getItem(STORAGE_KEY.LIBRARIES),
      StorageAdapter.getItem(STORAGE_KEY.BOOKS)
    ).subscribe(([libraries, books]) => {
      if (!libraries) {
        StorageAdapter.setItem(STORAGE_KEY.LIBRARIES, []);
      }

      if (!books) {
        StorageAdapter.setItem(STORAGE_KEY.BOOKS, []);
      }
    });
  }

  /**
   * Get data from localStorage and pars it from JSON
   * @param {string} key
   */
  static getItem<T>(key: STORAGE_KEY): Observable<T> {
    let value: T;

    try {
      value = JSON.parse(window.localStorage.getItem(key));
    } catch (e) {
      return throwError('Incorrect data');
    }

    return of(value);
  }

  /**
   * Save data to localStorage and convert it to String
   * @param {string} key
   * @param {T} value
   * @returns {Observable<T>}
   */
  static setItem(key: STORAGE_KEY, value: any): Observable<any> {
    window.localStorage.setItem(key, JSON.stringify(value));
    return of(value);
  }

  /**
   * Delete data from local storage
   * @param {string} key
   */
  static delete(key: STORAGE_KEY): void {
    return window.localStorage.removeItem(<any>key);
  }

  /**
   * Get entity by id from the LocalStorage
   * @param {STORAGE_KEY} key
   * @param {string} id
   * @returns {Observable<T extends IId>}
   */
  static getEntityById<T extends IId>(key: STORAGE_KEY, id: string): Observable<T> {
    return StorageAdapter.getItem(key)
      .pipe(
        map((entities: T[] = []) => {
          return entities.find((entity: T) => entity.id === id);
        })
      );
  }

  /**
   * Add a new entity to the LocalStorage
   * @param {STORAGE_KEY} key
   * @param {T} entity
   * @returns {Observable<T[]>}
   */
  static addEntity<T>(key: STORAGE_KEY, entity: T): Observable<T[]> {
    return StorageAdapter.getItem(key)
      .pipe(
        mergeMap((entities: T[] = []) => {
          return StorageAdapter.setItem(key, [...entities, entity]);
        })
      );
  }

  /**
   * Update existed entity in the LocalStorage
   * @param {STORAGE_KEY} key
   * @param {T} entity
   * @returns {Observable<T[]>} - full changed list
   */
  static updateEntity<T extends IId>(key: STORAGE_KEY, entity: T): Observable<T[]> {
    return StorageAdapter.getItem(key)
      .pipe(
        mergeMap((entities: T[] = []) => {
          const index = entities.findIndex((v) => v.id === entity.id);

          if (index > -1) {
            entities[index] = entity;
            return StorageAdapter.setItem(key, entities);
          } else {
            return entities;
          }
        })
      );
  }

  /**
   * Delete existed entity in the LocalStorage
   * @param {STORAGE_KEY} key
   * @param {string} id
   * @returns {Observable<T[]>} - full changed list
   */
  static deleteEntity<T extends IId>(key: STORAGE_KEY, id: string): Observable<T[]> {
    return StorageAdapter.getItem(key)
      .pipe(
        mergeMap((entities: T[] = []) => {
          const index = entities.findIndex((v) => v.id === id);

          if (index > -1) {
            entities.splice(index, 1);
            return StorageAdapter.setItem(key, entities);
          } else {
            return entities;
          }
        })
      );
  }

  /**
   * Get data from localStorage and pars it from JSON
   * @param {string} key
   */
  static getItemPrimitive<T>(key: STORAGE_KEY): T | Error {
    let value: T;

    try {
      value = JSON.parse(window.localStorage.getItem(key));
    } catch (e) {
      return new Error('Incorrect data');
    }

    return value;
  }

}
