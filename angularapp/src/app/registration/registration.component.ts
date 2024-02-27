import { Component } from '@angular/core';
import { FormsModule, FormGroup, ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { UserService } from '../user.service';
import { User } from '../user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-registration',
  standalone: true,
  imports: [FormsModule, ReactiveFormsModule],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {
  registerForm: FormGroup;
  constructor(
    private userService: UserService,
    private fb: FormBuilder,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      email: ['', Validators.required],
      username: ['', Validators.required],
      password: ['', Validators.required],
      passwordRetype: ['', Validators.required],
      surname: ['', Validators.required],
      firstname: ['', Validators.required]
    });
  }

  register() {
    const val = this.registerForm.value;
    if (val.password == val.passwordRetype) {
      const user: User = { id: "", email: val.email, password: val.password, username: val.username, surname: val.surname, firstname: val.firstname };
      this.userService.register(user).subscribe(
        (data) => {
          console.log(data);
          this.router.navigateByUrl('/');
        });
    }
  }
}
