import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {APP_INITIALIZER, CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {ServiceWorkerModule} from '@angular/service-worker';
import {BrowserModule} from '@angular/platform-browser';
import {environment} from '../environments/environment';

import {AppComponent} from './app.component';
import {GameModule} from './game/game.module';
import {SharedModule} from './shared/shared.module';
import {StorageAdapter} from './shared/adapters/storage.adapter';

export function InitFactory() {
  return function () {
    StorageAdapter.init();
  };
}

/**
 * The bootstrapper module
 */
@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    GameModule,
    SharedModule,
    ServiceWorkerModule.register('/fw/ngsw-worker.js', {enabled: environment.production})
  ],
  providers: [
    {
      provide: APP_INITIALIZER,
      useFactory: InitFactory,
      deps: [],
      multi: true
    }
  ],
  bootstrap: [
    AppComponent
  ],
  schemas: [
    CUSTOM_ELEMENTS_SCHEMA
  ]
})
export class AppModule {
}
