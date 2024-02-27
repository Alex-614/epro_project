import { AfterViewInit, Component, ElementRef, ViewChild} from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../user.service';
import { CompanyService } from '../company.service';
import { Router } from '@angular/router';
import { NgFor } from '@angular/common';
import { Company } from '../company';

@Component({
  selector: 'app-company-management',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, NgFor],
  templateUrl: './company-management.component.html',
  styleUrl: './company-management.component.css'
})
export class CompanyManagementComponent implements AfterViewInit{
  createCompany: FormGroup;
  changeCompanyName: FormGroup;
  deleteCompany: FormGroup;
  setActiveCompany: FormGroup;
  addCompanyUser: FormGroup;
  companies: Company[] = [];

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private companyService: CompanyService,
    private router: Router
  ) {
    this.createCompany = this.fb.group({
      companyName: ['', Validators.required]
    });
    this.changeCompanyName = this.fb.group({
      companyId: ['', Validators.required],
      newCompanyName: ['', Validators.required]
    });
    this.deleteCompany = this.fb.group({
      companyId: ['', Validators.required]
    });
    this.setActiveCompany = this.fb.group({
      companyId: ['', Validators.required]
    });
    this.addCompanyUser = this.fb.group({
      companyId: ['', Validators.required],
      userId: ['', Validators.required],
      role: ['', Validators.required]
    });
  }

  ngAfterViewInit(){
    console.log("ngAfterViewInit");
    this.userService.getCompanies().subscribe(data => {
      this.companies = [];
      for (var co of data){
        this.companies.push({id: co.id, name: co.name});
      }
    });
  }

  btn_createCompany(){
    const val = this.createCompany.value;
    if (val.companyName) {
      this.companyService.createCompany(val.companyName).subscribe(data => {
        console.log(data);
        this.ngAfterViewInit();
      });
    }
  }

  btn_changeCompanyName(){
    const val = this.changeCompanyName.value;
    if (val.companyId && val.newCompanyName){
      this.companyService.changeCompanyName(val.companyId, val.newCompanyName).subscribe(data => {
        console.log(data);
        this.ngAfterViewInit();
      });
    }
  }

  btn_deleteCompany(){
    const val = this.deleteCompany.value;
    if (val.companyId) {
      this.companyService.deleteCompany(val.companyId).subscribe(data => {
        this.ngAfterViewInit();
      });
    }
  }

  btn_setActiveCompany(){
    const val = this.setActiveCompany.value;
    if (val.companyId) {
      this.companyService.getCompany(val.companyId).subscribe(data => {
      localStorage.setItem("activeCompanyId", data.id);
      localStorage.setItem("activeCompanyName", data.name);
      });
    }
  }

  btn_addCompanyUser(){
    const val = this.addCompanyUser.value;
    if (val.companyId && val.userId && val.role){
      this.companyService.addCompanyUser(val.companyId, val.userId, val.role).subscribe(data => {
        console.log(data);
      });
    }
  }
}
