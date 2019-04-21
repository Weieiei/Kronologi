import { TestBed } from '@angular/core/testing';

import { ClientGuard } from './client.guard';

describe('ClientGuard', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: ClientGuard = TestBed.get(ClientGuard);
    expect(service).toBeTruthy();
  });
});
