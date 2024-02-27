import { Component, OnInit } from '@angular/core';
import { NgFor } from '@angular/common';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Businessunitobjective } from '../businessunitobjective';
import { BukrService } from '../bukr.service';
import { Businessunitkeyresult } from '../businessunitkeyresult';
import { BusinessunitService } from '../businessunit.service';
import { Businessunit } from '../businessunit';
import { Bukrtable } from '../bukrtable';

@Component({
  selector: 'app-buokrmanagement',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, NgFor],
  templateUrl: './buokrmanagement.component.html',
  styleUrl: './buokrmanagement.component.css'
})
export class BuokrmanagementComponent implements OnInit {
  createBusinessunitObjective: FormGroup;
  createBusinessunitKeyresult: FormGroup;
  updateBusinessunitKeyresult: FormGroup;
  activeCompany: string = "";
  businessunits: Businessunit[] = [];
  businessunitObjectives: Businessunitobjective[] = [];
  businessunitKeyresults: Bukrtable[] = [];

  constructor(
    private fb: FormBuilder,
    private bukrService: BukrService,
    private buService: BusinessunitService
  ) {
    this.createBusinessunitObjective = this.fb.group({
      buoBuId: ['', Validators.required],
      buoTitle: ['', Validators.required],
      buoDescription: ['', Validators.required],
      buoDeadline: ['', Validators.required],
      buoCkr: ['', Validators.required]
    });
    this.createBusinessunitKeyresult = this.fb.group({
      bukrBuId: ['', Validators.required],
      bukrBuoId: ['', Validators.required],
      bukrTitle: ['', Validators.required],
      bukrType: ['', Validators.required],
      bukrCurrent: ['', Validators.required],
      bukrGoal: ['', Validators.required],
      bukrConfidenceLevel: ['', Validators.required],
      bukrDescription: ['', Validators.required],
      bukrContributingUnits: ['', Validators.required]
    });
    this.updateBusinessunitKeyresult = this.fb.group({
      ubukrBuId: ['', Validators.required],
      ubukrBuoId: ['', Validators.required],
      ubukrBuKrId: ['', Validators.required],
      ubukrStatusUpdate: ['', Validators.required],
      ubukrCurrent: ['', Validators.required],
      ubukrConfidenceLevel: ['', Validators.required],
      ubukrContributingUnits: ['', Validators.required]
    })
  }

  ngOnInit() {
    console.log("ngOnInit");
    this.activeCompany = localStorage.getItem("activeCompanyName")!;
    this.buService.getBusinessUnits().subscribe(data => {
      console.log(data);
      this.businessunits = [];
      this.businessunitObjectives = [];
      this.businessunitKeyresults = [];
      console.log(data);
      for (var bu of data) {
        this.businessunits.push(bu);
        this.fillObjectives(bu);
      }
    });
  }

  fillObjectives(bu: Businessunit) {
    this.bukrService.getBusinessunitObjectives(bu).subscribe(data => {
      console.log(data);
      for (var buo of data) {
        this.businessunitObjectives.push({
          buId: bu.id.id,
          id: buo.id,
          title: buo.title,
          description: buo.description,
          deadline: new Date(buo.deadline).toLocaleString("de-DE", { timeZone: "Europe/Berlin" }),
          ownerId: buo.ownerId,
          achievement: buo.achievement,
          companyKeyResult_ID: buo.companyKeyResult_ID
        });
        this.fillKeyresults(bu, buo);
      }
    });
  }

  fillKeyresults(bu: Businessunit, buo: Businessunitobjective) {
    this.bukrService.getBusinessunitKeyresults(bu, buo).subscribe(data => {
      this.businessunitKeyresults.push({
        overallColorClass: this.getOverallColorClass(buo.achievement),
        overall: buo.achievement + "%",
        id: buo.id.toString(),
        title: buo.title,
        current: "",
        goal: "",
        confidenceLevel: "",
        owner: buo.ownerId.toString(),
        description: "",
        contributingUnits: ""
      });
      for (var kr of data) {
        var contrUnitsString: string = "";
        for (var unit of kr.contributingUnits) {
          contrUnitsString += unit.id + ", "
        }
        this.businessunitKeyresults.push({
          overallColorClass: this.getOverallColorClass(kr.achievement),
          overall: kr.achievement + "%",
          id: ">KR: " + kr.id.toString(),
          title: kr.title,
          current: kr.current.toString(),
          goal: kr.goal.toString(),
          confidenceLevel: kr.confidenceLevel.toString(),
          owner: "",
          description: kr.description,
          contributingUnits: contrUnitsString
        });
      }
    });
  }

  btn_createBusinessunitObjective() {
    const val = this.createBusinessunitObjective.value;
    var buo: Businessunitobjective;
    if (val.buoCkr) {
      buo = {
        buId: val.buoBuId,
        id: NaN,
        title: val.buoTitle,
        description: val.buoDescription,
        deadline: (new Date(val.buoDeadline)).getTime().toString(),
        ownerId: parseInt(localStorage.getItem("userId")!),
        achievement: 0,
        companyKeyResult_ID: val.buoCkr
      }
    } else {
      buo = {
        buId: val.buoBuId,
        id: NaN,
        title: val.buoTitle,
        description: val.buoDescription,
        deadline: (new Date(val.buoDeadline)).getTime().toString(),
        ownerId: parseInt(localStorage.getItem("userId")!),
        achievement: 0,
        companyKeyResult_ID: ""
      }
    }
    this.bukrService.createBusinessunitObjective(buo!).subscribe(data => {
      console.log(data);
      this.ngOnInit();
    });
  }

  btn_createBusinessunitKeyresult() {
    const val = this.createBusinessunitKeyresult.value;
    const unitsArray = val.bukrContributingUnits.split(" ");
    console.log(unitsArray);
    var contributingUnitsArray: { id: number, businessUnitId: { id: number, companyId: number } }[] = [];
    for (var unit of unitsArray) {
      contributingUnitsArray.push({
        id: parseInt(unit),
        businessUnitId: {
          id: val.bukrBuId,
          companyId: parseInt(localStorage.getItem("activeCompanyId")!)
        }
      });
    }
    console.log(contributingUnitsArray);


    const bukr: Businessunitkeyresult = {
      buId: val.bukrBuId,
      buoId: val.bukrBuoId,
      id: NaN,
      title: val.bukrTitle,
      type: val.bukrType,
      goal: val.bukrGoal,
      description: val.bukrDescription,
      current: val.bukrCurrent,
      confidenceLevel: val.bukrConfidenceLevel,
      achievement: NaN,
      contributingUnits: contributingUnitsArray,
      contributingBusinessUnits: []
    }
    this.bukrService.createBusinessunitKeyresult(bukr).subscribe(data => {
      console.log(data);
      this.ngOnInit();
    });
  }

  btn_updateBusinessunitKeyresult() {
    const val = this.updateBusinessunitKeyresult.value;
    const unitsArray = val.ubukrContributingUnits.split(" ");
    console.log(unitsArray);
    var contributingUnitsArray: { id: number, businessUnitId: { id: number, companyId: number } }[] = [];
    for (var unit of unitsArray) {
      contributingUnitsArray.push({
        id: parseInt(unit),
        businessUnitId: {
          id: val.ubukrBuId,
          companyId: parseInt(localStorage.getItem("activeCompanyId")!)
        }
      });
    }
    console.log(contributingUnitsArray);


    const bukr: Businessunitkeyresult = {
      buId: val.ubukrBuId,
      buoId: val.ubukrBuoId,
      id: val.ubukrBuKrId,
      title: "",
      type: "",
      goal: NaN,
      description: "",
      current: val.ubukrCurrent,
      confidenceLevel: val.ubukrConfidenceLevel,
      achievement: NaN,
      contributingUnits: contributingUnitsArray,
      contributingBusinessUnits: []
    }
    this.bukrService.updateBusinessunitKeyresult(bukr, val.ubukrStatusUpdate).subscribe(data => {
      console.log(data);
      this.ngOnInit();
    });
  }

  getOverallColorClass(achivment: number) {
    var result = "";
    if (achivment >= 66) {
      result = "bgGreen";
    } else {
      if (achivment >= 33) {
        result = "bgOrange";
      } else {
        result = "bgRed";
      }
    }
    return result;
  }
}
