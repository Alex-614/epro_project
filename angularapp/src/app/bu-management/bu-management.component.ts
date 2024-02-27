import { AfterViewInit, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { Businessunit } from '../businessunit';
import { Router } from '@angular/router';
import { BusinessunitService } from '../businessunit.service';
import { NgFor } from '@angular/common';
import { Unit } from '../unit';

@Component({
  selector: 'app-bu-management',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, NgFor],
  templateUrl: './bu-management.component.html',
  styleUrl: './bu-management.component.css'
})
export class BuManagementComponent implements OnInit{
  createBusinessUnit: FormGroup;
  createUnit: FormGroup;
  changeBuName: FormGroup;
  deleteBusinessUnit: FormGroup;
  businessUnits: {id: string, name: string}[] = [];
  activeCompany = "";

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private buService: BusinessunitService
  ) {
    this.createBusinessUnit = this.fb.group({
      buName: ['', Validators.required]
    });
    this.createUnit = this.fb.group({
      ubuId: ['', Validators.required],
      uName: ['', Validators.required]
    })
    this.changeBuName = this.fb.group({
      buId: ['', Validators.required],
      newBuName: ['', Validators.required]
    });
    this.deleteBusinessUnit = this.fb.group({
      buId: ['', Validators.required]
    });
  }

  ngOnInit(){
    console.log("ngAfterViewInit");
    this.activeCompany = localStorage.getItem("activeCompanyName")!;
    this.buService.getBusinessUnits().subscribe(data => {
      this.businessUnits = [];
      for (var bu of data){
        this.fillUnits(bu.id.id, bu.name);
      }
    });
  }

  fillUnits(buId: number, buName: string){
    this.buService.getUnits(buId).subscribe(data => {
      this.businessUnits.push({id: "BU: " + buId, name: buName});
      for (var unit of data){
        this.businessUnits.push({id: " > Unit:  " + unit.id.id, name: unit.name});
      }
    });
  }

  btn_createBusinessUnit(){
    const val = this.createBusinessUnit.value;
    if (val.buName) {
      this.buService.createBusinessUnit(val.buName).subscribe(data => {
        console.log(data);
        this.ngOnInit();
      });
    }
  }

  btn_changeBuName(){
    const val = this.changeBuName.value;
    if (val.buId && val.newBuName){
      this.buService.changeBuName(val.buId, val.newBuName).subscribe(data => {
        console.log(data);
        this.ngOnInit();
      });
    }
  }

  btn_deleteBusinessUnit(){
    const val = this.deleteBusinessUnit.value;
    if (val.buId) {
      this.buService.deleteBusinessUnit(val.buId).subscribe(data => {
        this.ngOnInit();
      });
    }
  }

  btn_createUnit(){
    const val = this.createUnit.value;
    if (val.ubuId && val.uName){
      this.buService.createUnit(val.ubuId, val.uName).subscribe(data => {
        this.ngOnInit();
      });
    }
  }
}
