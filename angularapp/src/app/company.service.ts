import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Company } from './company';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {
  private url: string = "http://localhost:8080/api";

  constructor(
    private http: HttpClient
  ) { }

  public createCompany(companyName: string) {
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.post<Company>(this.url + "/company", { "name": companyName }, header);
  }

  public changeCompanyName(companyId: string, newCompanyName: string) {
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.patch<Company>(this.url + "/company/" + companyId, { "name": newCompanyName }, header);
  }

  public deleteCompany(companyId: string) {
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.delete(this.url + "/company/" + companyId, header);
  }

  public getCompany(companyId: string) {
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.get<Company>(this.url + "/company/" + companyId, header);
  }

  public addCompanyUser(companyId: string, userId: string, roleId: number) {
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.post(this.url + "/company/" + companyId + "/user/" + userId + "/add", { "roleIds": [roleId] }, header);
  }
}