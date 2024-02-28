import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { CokrService } from '../cokr.service';
import { Companyobjective } from '../companyobjective';
import { NgFor } from '@angular/common';
import { Companykeyresult } from '../companykeyresult';
import { Cokrtable } from '../cokrtable';
import { Objectivetable } from '../objectivetable';

@Component({
  selector: 'app-cokrmanagement',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, NgFor],
  templateUrl: './cokrmanagement.component.html',
  styleUrl: './cokrmanagement.component.css'
})
export class CokrmanagementComponent implements OnInit {
  createCompanyObjective: FormGroup;
  createCompanyKeyresult: FormGroup;
  updateKeyresult: FormGroup;
  activeCompany: string = "";
  companyObjectives: Objectivetable[] = [];
  companyObjectiveKeyresults: Cokrtable[] = [];

  constructor(
    private fb: FormBuilder,
    private cokrService: CokrService
  ) {
    this.createCompanyObjective = this.fb.group({
      coTitle: ['', Validators.required],
      coDescription: ['', Validators.required],
      coDeadline: ['', Validators.required]
    });
    this.createCompanyKeyresult = this.fb.group({
      ckCoId: ['', Validators.required],
      ckTitle: ['', Validators.required],
      ckType: ['', Validators.required],
      ckCurrent: ['', Validators.required],
      ckGoal: ['', Validators.required],
      ckConfidenceLevel: ['', Validators.required],
      ckDescription: ['', Validators.required],
      ckContributingBusinessUnits: ['', Validators.required]
    });
    this.updateKeyresult = this.fb.group({
      ukrCoId: ['', Validators.required],
      ukrId: ['', Validators.required],
      ukrStatusUpdate: ['', Validators.required],
      ukrCurrent: ['', Validators.required],
      ukrConfidenceLevel: ['', Validators.required],
      ukrContributingBusinessUnits: ['', Validators.required]
    });
  }

  ngOnInit() {
    console.log("ngOnInit");
    this.activeCompany = localStorage.getItem("activeCompanyName")!;
    this.cokrService.getCompanyObjectives(parseInt(localStorage.getItem("activeCompanyId")!)).subscribe(data => {
      this.companyObjectives = [];
      this.companyObjectiveKeyresults = [];
      console.log(data);
      for (var co of data) {
        this.companyObjectives.push({
          id: co.id,
          title: co.title,
          description: co.description,
          deadline: new Date(co.deadline).toLocaleString("de-DE", { timeZone: "Europe/Berlin" }),
          owner: co.ownerId.toString()
        });
        this.fillKeyresults(co.achievement, co.id, co.title, co.ownerId);
      }
    });
  }

  fillKeyresults(coAchivment: number, coId: number, coTitle: string, coOwnerId: number) {
    this.cokrService.getCompanyKeyresults(coId, localStorage.getItem("activeCompanyId")!).subscribe(data => {
      var dummyCokr: Cokrtable[] = [];
      dummyCokr.push({
        overallColorClass: this.getOverallColorClass(coAchivment),
        overall: coAchivment + "%",
        id: "CO: " + coId.toString(),
        title: coTitle,
        current: "",
        goal: "",
        confidenceLevel: "",
        owner: coOwnerId.toString(),
        description: "",
        contributingUnits: ""
      })
      for (var ckr of data) {
        var contrUnitsString: string = "";
        for (var unit of ckr.contributingBusinessUnits) {
          contrUnitsString += unit.id + ", "
        }
        dummyCokr.push({
          overallColorClass: this.getOverallColorClass(ckr.achievement),
          overall: ckr.achievement + "%",
          id: ">KR: " + ckr.id,
          title: ckr.title,
          current: ckr.current.toString(),
          goal: ckr.goal.toString(),
          confidenceLevel: ckr.confidenceLevel.toString(),
          owner: "",
          description: ckr.description,
          contributingUnits: contrUnitsString
        })
      }
      for (var ckrRow of dummyCokr) {
        this.companyObjectiveKeyresults.push(ckrRow);
      }
    });
  }

  btn_createCompanyObjective() {
    const val = this.createCompanyObjective.value;
    const co: Companyobjective = {
      id: NaN,
      title: val.coTitle,
      description: val.coDescription,
      deadline: (new Date(val.coDeadline)).getTime(),
      ownerId: parseInt(localStorage.getItem("userId")!),
      achievement: 0
    }
    this.cokrService.createCompanyObjective(co).subscribe(data => {
      console.log(data);
      this.ngOnInit();
    });
  }

  btn_createCompanyKeyresult() {
    const val = this.createCompanyKeyresult.value;
    const unitArray = val.ckContributingBusinessUnits.split(" ");
    var contributingBusinessUnitsArray = [];
    for (var unit of unitArray) {
      contributingBusinessUnitsArray.push({ id: unit, companyId: localStorage.getItem("activeCompanyId")! });
    }
    const ck: Companykeyresult = {
      id: "",
      title: val.ckTitle,
      type: val.ckType,
      goal: val.ckGoal,
      description: val.ckDescription,
      current: val.ckCurrent,
      confidenceLevel: val.ckConfidenceLevel,
      achievement: NaN,
      contributingBusinessUnits: contributingBusinessUnitsArray
    }
    this.cokrService.createCompanyKeyresult(ck, val.ckCoId).subscribe(data => {
      console.log(data);
      this.ngOnInit();
    });
  }

  btn_updateKeyresult() {
    const val = this.updateKeyresult.value;
    const unitArray = val.ukrContributingBusinessUnits.split(" ");
    var contributingBusinessUnitsArray = [];
    for (var unit of unitArray) {
      contributingBusinessUnitsArray.push({ id: unit, companyId: parseInt(localStorage.getItem("activeCompanyId")!) });
    }
    this.cokrService.updateKeyresult(val.ukrCoId, val.ukrId, val.ukrStatusUpdate, val.ukrCurrent, val.ukrConfidenceLevel, contributingBusinessUnitsArray).subscribe(data => {
      console.log(data);
      this.ngOnInit();
    });
  }

  //get table color of the percentual value of achievement
  getOverallColorClass(achivment: number) {
    var result = "";
    if (achivment >= 66) {
      result = "bgGreen";
    } else {
      if (achivment >= 33){
        result = "bgOrange";
      }else {
        result = "bgRed";
      }
    }
    return result;
  }

}