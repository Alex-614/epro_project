import { Component } from '@angular/core';
import { FormsModule, FormGroup, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { RouterLink, Router } from '@angular/router';
import { UserService } from '../user.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {

  loginForm: FormGroup;
  constructor(
    private userService: UserService,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  //save timeout time in local storage
  login() {
    const val = this.loginForm.value;
    if (val.email && val.password) {
      this.userService.login(val.email, val.password).subscribe(data => {
        console.log(data);
        console.log(Date.now());
        if (data.accessToken) {
          localStorage.clear;
          localStorage.setItem("accessToken", data.accessToken);
          localStorage.setItem("tokenExpire", (Date.now() + data.expirationTime).toString());
          localStorage.setItem("userId", data.userId!.toString());
          this.router.navigateByUrl('/homescreen');
        }
      });
    }
  }
}
