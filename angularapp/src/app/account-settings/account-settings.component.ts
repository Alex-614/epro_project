import { Component } from '@angular/core';
import { FormsModule, FormGroup, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-account-settings',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './account-settings.component.html',
  styleUrl: './account-settings.component.css'
})
export class AccountSettingsComponent {
  changeUsername: FormGroup;
  changePassword: FormGroup;
  changeName: FormGroup;
  constructor(
    private userService: UserService,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.changeUsername = this.fb.group({
      username: ['', Validators.required]
    });
    this.changePassword = this.fb.group({
      password: ['', Validators.required],
      passwordRetype: ['', Validators.required]
    });
    this.changeName = this.fb.group({
      firstname: ['', Validators.required],
      surname: ['', Validators.required]
    });
  }

  btn_changeUsername() {
    const val = this.changeUsername.value;
    if (val.username != "") {
      this.userService.changeUsername(val.username);
    }
  }

  btn_changePassword() {
    const val = this.changePassword.value;
    if (val.password != "" && val.password == val.passwordRetype) {
      this.userService.changePassword(val.password);
    }
  }

  btn_changeName() {
    const val = this.changeName.value;
    if (val.firstname != "" && val.surname != ""){
      this.userService.changeName(val.firstname, val.surname);
    }
  }

  btn_deleteUser() {
    this.userService.deleteUser().subscribe(data => {
      this.router.navigateByUrl("/");
      console.log(data);
    })
  }


}
