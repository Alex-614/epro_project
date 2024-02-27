import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BuokrmanagementComponent } from './buokrmanagement.component';

describe('BuokrmanagementComponent', () => {
  let component: BuokrmanagementComponent;
  let fixture: ComponentFixture<BuokrmanagementComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BuokrmanagementComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(BuokrmanagementComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
