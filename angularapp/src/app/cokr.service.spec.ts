import { TestBed } from '@angular/core/testing';

import { CokrService } from './cokr.service';

describe('CokrService', () => {
  let service: CokrService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CokrService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
