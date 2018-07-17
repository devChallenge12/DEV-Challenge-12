import {TestBed, async} from '@angular/core/testing';

import {TEST_MODULE} from '../test.module';
import {AppComponent} from './app.component';

describe('AppComponent', () => {

  beforeEach(async(TEST_MODULE));

  it('should create the app', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    const component: AppComponent = fixture.debugElement.componentInstance;
    expect(component).toBeTruthy();
  }));

  it('should have `Home` link', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelectorAll('.docs-navbar-header a')[0].textContent).toContain('Home');
  }));

  it('should have `Examples` link', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelectorAll('.docs-navbar-header a')[1].textContent).toContain('Examples');
  }));

  it('should have `Users` link', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelectorAll('.docs-navbar-header a')[2].textContent).toContain('Users');
  }));

  it('should have `Login` link', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('.login a').textContent).toContain('Login');
  }));

  it('should have `Login` button', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('.login button').textContent).toContain('Sign in');
  }));

  it('should exist .main with router-outlet', async(() => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('.main router-outlet')).toBeTruthy();
  }));

});
