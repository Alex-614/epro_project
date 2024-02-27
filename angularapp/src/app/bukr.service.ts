import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Businessunitobjective } from './businessunitobjective';
import { Businessunitkeyresult } from './businessunitkeyresult';
import { Businessunit } from './businessunit';

@Injectable({
  providedIn: 'root'
})
export class BukrService {
  private url: string = "http://localhost:8080/api";

  constructor(
    private http: HttpClient
  ) {
  }

  public createBusinessunitObjective(buo: Businessunitobjective) {
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    console.log(buo);
    return this.http.post<Businessunitobjective>(this.url + "/company/" + localStorage.getItem("activeCompanyId") + "/businessunit/" + buo.buId + "/objective", buo, header);
  }

  public createBusinessunitKeyresult(bukr: Businessunitkeyresult) {
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    console.log(bukr);
    return this.http.post(this.url + "/company/" + localStorage.getItem("activeCompanyId") + "/businessunit/" + bukr.buId + "/objective/" + bukr.buoId + "/keyresult",
      bukr, header);
  }

  public getBusinessunitObjectives(bu: Businessunit){
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.get<Businessunitobjective[]>(this.url + "/company/" + localStorage.getItem("activeCompanyId") + "/businessunit/" + bu.id.id + "/objective", header);
  }

  public getBusinessunitKeyresults(bu: Businessunit, buo: Businessunitobjective) {
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.get<Businessunitkeyresult[]>(this.url + "/company/" + localStorage.getItem("activeCompanyId") + "/businessunit/" + bu.id.id + "/objective/" + buo.id + "/keyresult", header);
  }

  public updateBusinessunitKeyresult(bukr: Businessunitkeyresult, statusUpdate: string) {
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    console.log(bukr.contributingUnits);
    return this.http.patch(this.url + "/company/" + localStorage.getItem("activeCompanyId") + "/businessunit/" + bukr.buId + "/objective/" + bukr.buoId + "/keyresult/" + bukr.id,
      {
        statusUpdate: statusUpdate,
        keyResultDto: {
          current: bukr.current,
          confidenceLevel: bukr.confidenceLevel,
          contributingUnits: bukr.contributingUnits
        }
      }, header);
  }
}
