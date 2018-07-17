import { Injectable } from '@angular/core';
import { Http, Headers, Response, RequestOptions, ResponseType } from '@angular/http';
import { Observable } from 'rxjs';
import { Subject } from 'rxjs';
import { Md5 } from 'ts-md5/dist/md5';
import { map } from 'rxjs/operators';

@Injectable()
export class AuthenticationService {
    public token: string;
    public loginFlag: boolean;
    public this = this;
    private subject = new Subject<any>();
    private sKey: string = 'flsjlfkjas fadslfjkalfkja afljdf;lkjafafd lsafja;fj'
                            + 'fsldfjs UUUL lKJLKJ OO jLLJ 435487 fdsflsjdf';
    private userList: object[] = [{name: 'admin', pass: 'e00dfb0ced1b09fce1608483fd73e4af'}];

    constructor(private http: Http) {}

    public sendMessage(message: string) {
        this.subject.next({ text: message });
    }

    public clearMessage() {
        this.subject.next();
    }

    public getMessage(): Observable<any> {
        return this.subject.asObservable();
    }

    /*************************************************************************** */
    /**************** Функція авторизації ************************ */
    /************************************************************************** */
    public login(username: string, password: string) {
        let loginStatus: boolean = false;
        for (let user of this.userList) {
            if (user['name'] === username) {
                if (user['pass'] === Md5.hashStr(password + this.sKey)) {
                    window.localStorage.setItem('library_user', username);
                    let usernameKey = Md5.hashStr(username + this.sKey).toString();
                    window.localStorage.setItem('library_key', usernameKey);
                    loginStatus = true;
                }
            }
        }
        return loginStatus;
    }

    /*************************************************************************** */
    /**************** Функція перевірки авторизації ************************ */
    /************************************************************************** */
    public check() {
        let user = localStorage.getItem('library_user');
        let key = localStorage.getItem('library_key');
        if (user !== '' && key !== '' && Md5.hashStr(user + this.sKey) === key) {
            return true;
        } else {
            return false;
        }
    }
}
