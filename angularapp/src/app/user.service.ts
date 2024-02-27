import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { AccessToken } from './access-token';
import { Company } from './company';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private url: string = "http://localhost:8080/api";

  constructor(
    private http: HttpClient
  ) {
  }

  public register<User>(user: User) {
    console.log("register in user service");
    return this.http.post<User>(this.url + "/user", user);
  }

  public login(email: string, password: string) {
    return this.http.post<AccessToken>(this.url + "/login", { email, password });
  }

  public logout(){
    localStorage.clear();
  }

  public isLoggedIn() {
    const tokenExpire = parseInt(localStorage.getItem("tokenExpire")!);
    if (tokenExpire) {
      if (tokenExpire > Date.now()) {
        console.log("isLoggedIn True");
        return true;
      }
    }
    console.log("isLoggedIn False");
    return false;
  }

  public getUser() {
    const userId = localStorage.getItem("userId");
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.get<User>(this.url + "/user/" + userId, header);
  }

  public getUserById(userId: number) {
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.get<User>(this.url + "/user/" + userId, header);
  }

  public changeUsername(username: string) {
    const userId = localStorage.getItem("userId");
    console.log("changename" + userId + username);
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    this.http.get<User>(this.url + "/user/" + userId, header).subscribe(data => {
      this.http.patch(this.url + "/user/" + userId, {
        "username": username
      }, header).subscribe(data => console.log(data));
    });
  }

  public changePassword(password: string) {
    const userId = localStorage.getItem("userId");
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    this.http.get<User>(this.url + "/user/" + userId, header).subscribe(data => {
      this.http.patch(this.url + "/user/" + userId, {
        "password": password
      }, header).subscribe(data => console.log(data));
    });
  }

  public changeName(firstname: string, surname: string) {
    const userId = localStorage.getItem("userId");
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    this.http.get<User>(this.url + "/user/" + userId, header).subscribe(data => {
      this.http.patch(this.url + "/user/" + userId, {
        "firstname": firstname, "surname": surname
      }, header).subscribe(data => console.log(data));
    });
  }

  //TODO: remove Delete Type from rest answer
  public deleteUser() {
    const userId = localStorage.getItem("userId");
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    localStorage.clear;
    return this.http.delete(this.url + "/user/" + userId, header);
  }

  public getCompanies() {
    const userId = localStorage.getItem("userId");
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.get<Company[]>(this.url + "/user/" + userId + "/companies", header);
  }

}
