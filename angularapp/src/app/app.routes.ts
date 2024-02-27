import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { RegistrationComponent } from './registration/registration.component';
import { HomescreenComponent } from './homescreen/homescreen.component';
import { AccountSettingsComponent } from './account-settings/account-settings.component';
import { CompanyManagementComponent } from './company-management/company-management.component';
import { BuManagementComponent } from './bu-management/bu-management.component';
import { CokrmanagementComponent } from './cokrmanagement/cokrmanagement.component';
import { BuokrmanagementComponent } from './buokrmanagement/buokrmanagement.component';

export const routes: Routes = [
    {
        path: '',
        component: LoginComponent,
        title: 'Login page'
    },
    {
        path: 'registration',
        component: RegistrationComponent,
        title: 'Registration'
    },
    {
        path: 'homescreen',
        component: HomescreenComponent,
        title: 'Homescreen'
    },
    {
        path: 'homescreen/account-settings',
        component: AccountSettingsComponent,
        title: 'Account-Settings'
    },
    {
        path: 'homescreen/company-management',
        component: CompanyManagementComponent,
        title: 'Company-Management'
    },
    {
        path: 'homescreen/bu-management',
        component: BuManagementComponent,
        title: 'Business-Unit-Management'
    },
    {
        path: 'homescreen/cokr-management',
        component: CokrmanagementComponent,
        title: 'COKR-Management'
    },
    {
        path: 'homescreen/buokr-management',
        component: BuokrmanagementComponent,
        title: 'BUOKR-Management'
    }
];
