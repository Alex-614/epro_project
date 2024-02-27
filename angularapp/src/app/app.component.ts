import { AfterViewInit, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterOutlet } from '@angular/router';
import { UserService } from './user.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent{
  title = 'angularapp';
  companySelected = localStorage.getItem("activeCompany");
  constructor(
    private router: Router,
    private userService: UserService
  ){}

  gotoHome(){
    if (this.userService.isLoggedIn()){
      this.router.navigateByUrl("/homescreen");
    }else {
      this.router.navigateByUrl("/");
    }
  }
}
