import { TestBed } from '@angular/core/testing';

import { BusinessunitService } from './businessunit.service';

describe('BusinessunitService', () => {
  let service: BusinessunitService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BusinessunitService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
