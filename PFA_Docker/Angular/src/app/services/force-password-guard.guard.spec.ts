import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { forcePasswordGuardGuard } from './force-password-guard.guard';

describe('forcePasswordGuardGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => forcePasswordGuardGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
