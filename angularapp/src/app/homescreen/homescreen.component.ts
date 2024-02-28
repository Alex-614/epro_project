import { AfterViewInit, Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../user.service';

@Component({
  selector: 'app-homescreen',
  standalone: true,
  imports: [],
  templateUrl: './homescreen.component.html',
  styleUrl: './homescreen.component.css'
})
export class HomescreenComponent implements AfterViewInit {
  username = "";
  userId = "";
  activeCompany = "";
  constructor(
    private router: Router,
    private userService: UserService
  ) {

  }

  ngAfterViewInit() {
    console.log("ngAfterViewInit");
    this.userService.getUser().subscribe(data => {
      this.username = data.username;
      this.userId = localStorage.getItem("userId")!;
      this.activeCompany = localStorage.getItem("activeCompanyName")!;
    });
  }

  //always check if user is logged in

  btn_accountSettings() {
    if (this.userService.isLoggedIn()) {
      this.router.navigateByUrl('/homescreen/account-settings');
    }
  }

  btn_companyManagement() {
    if (this.userService.isLoggedIn()) {
      this.router.navigateByUrl('/homescreen/company-management');
    }
  }

  btn_cokrManagement() {
    if (this.userService.isLoggedIn()) {
      this.router.navigateByUrl('/homescreen/cokr-management');
    }
  }

  btn_buManagement() {
    if (this.userService.isLoggedIn()) {
      this.router.navigateByUrl('/homescreen/bu-management');
    }
  }

  btn_buokrManagement() {
    if (this.userService.isLoggedIn()){
      this.router.navigateByUrl('/homescreen/buokr-management');
    }
  }

  btn_showOkrs() {
    if (this.userService.isLoggedIn()) {
      this.router.navigateByUrl('/homescreen/show-okrs');
    }
  }

  btn_logout() {
    this.userService.logout();
    this.router.navigateByUrl('/');
  }
}
