import { TestBed, async, inject } from '@angular/core/testing';

import { AnonymousGuard } from './anonymous.guard';
import { HttpClientModule } from '@angular/common/http';
import { RouterTestingModule } from '@angular/router/testing';

describe('AnonymousGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        RouterTestingModule
      ],
      providers: [AnonymousGuard]
    });
  });

  it('should ...', inject([AnonymousGuard], (guard: AnonymousGuard) => {
    expect(guard).toBeTruthy();
  }));
});
