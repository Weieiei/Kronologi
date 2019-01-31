import { TestBed } from '@angular/core/testing';

import { AvailabilityService } from './availability.service';

describe('AvailabilityService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AvailabilityService = TestBed.get(AvailabilityService);
    expect(service).toBeTruthy();
  });
});
