import { TestBed } from '@angular/core/testing';

import { DisponibilitaService } from './disponibilita.service';

describe('DisponibilitaService', () => {
  let service: DisponibilitaService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DisponibilitaService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
