import { TestBed } from '@angular/core/testing';

import { BukrService } from './bukr.service';

describe('BukrService', () => {
  let service: BukrService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BukrService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
