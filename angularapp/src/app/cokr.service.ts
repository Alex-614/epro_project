import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Companyobjective } from './companyobjective';
import { Companykeyresult } from './companykeyresult';

@Injectable({
  providedIn: 'root'
})
export class CokrService {
  private url: string = "http://localhost:8080/api";

  constructor(
    private http: HttpClient
  ) {
  }

  public createCompanyObjective<Companyobjective>(co: Companyobjective) {
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.post<Companyobjective>(this.url + "/company/" + localStorage.getItem("activeCompanyId") + "/objective", co, header);
  }

  public createCompanyKeyresult(ck: Companykeyresult, coId: string) {
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    console.log(ck);
    return this.http.post(this.url + "/company/" + localStorage.getItem("activeCompanyId") + "/objective/" + coId + "/keyresult",
      {
        title: ck.title, type: ck.type, goal: ck.goal, description: ck.description, current: ck.current, confidenceLevel: ck.confidenceLevel, representers: [], contributingUnits: [], contributingBusinessUnits: ck.contributingBusinessUnits
      }, header);
  }

  public getCompanyObjectives(companyId: number) {
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.get<Companyobjective[]>(this.url + "/company/" + companyId + "/objective", header);
  }

  public getCompanyKeyresults(coId: number, companyId: string) {
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    return this.http.get<Companykeyresult[]>(this.url + "/company/" + companyId + "/objective/" + coId + "/keyresult", header);
  }

  public updateKeyresult(coId: number, krId: number, krStatusUpdate: string, krCurrent: number, krConfidenceLevel: number, unitArray: { id: number, companyId: number }[]) {
    var header = {
      headers: new HttpHeaders()
        .set("Authorization", "Bearer " + localStorage.getItem("accessToken"))
    };
    console.log(krCurrent);
    return this.http.patch(this.url + "/company/" + localStorage.getItem("activeCompanyId") + "/objective/" + coId + "/keyresult/" + krId,
      {
        statusUpdate: krStatusUpdate,
        keyResultDto: {
          current: krCurrent,
          confidenceLevel: krConfidenceLevel,
          contributingBusinessUnits: unitArray
        }
      }, header);
  }
}
