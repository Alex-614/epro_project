import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Businessunit } from './businessunit';
import { Unit } from './unit';

@Injectable({
  providedIn: 'root'
})
export class BusinessunitService {
  private url: string = "http://localhost:8080/api";

  constructor(
    private http: HttpClient
  ) { }

  public createBusinessUnit(buName: string) {
    const activeCompanyId = localStorage.getItem("activeCompanyId");
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.post<Businessunit>(this.url + "/company/" + activeCompanyId + "/businessunit", { "name": buName }, header);
  }

  public changeBuName(buId: string, newBuName: string) {
    const activeCompanyId = localStorage.getItem("activeCompanyId");
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.patch<Businessunit>(this.url + "/company/" + activeCompanyId + "/businessunit/" + buId, { "name": newBuName }, header);
  }

  //TODO: remove Delete Type from rest answer
  public deleteBusinessUnit(buId: string){
    const activeCompanyId = localStorage.getItem("activeCompanyId");
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.delete(this.url + "/company/" + activeCompanyId + "/businessunit/" + buId, header);
  }

  public getBusinessUnits() {
    const activeCompanyId = localStorage.getItem("activeCompanyId");
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.get<Businessunit[]>(this.url + "/company/" + activeCompanyId + "/businessunit", header);
  }

  public createUnit(buId: number, uName: string){
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.post<Unit>(this.url + "/company/" + localStorage.getItem("activeCompanyId")! + "/businessunit/" + buId + "/unit",
    {name: uName}, header)
  }

  public getUnits(buId: number){
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.get<{id: {id: string}, name: string}[]>(this.url + "/company/" + localStorage.getItem("activeCompanyId")! + "/businessunit/" + buId + "/unit", header);
  }
}
