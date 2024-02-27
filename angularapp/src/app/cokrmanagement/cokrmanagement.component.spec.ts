import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CokrmanagementComponent } from './cokrmanagement.component';

describe('CokrmanagementComponent', () => {
  let component: CokrmanagementComponent;
  let fixture: ComponentFixture<CokrmanagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CokrmanagementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(CokrmanagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
