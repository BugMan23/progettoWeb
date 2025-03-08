import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IndirizziComponent } from './indirizzi.component';

describe('IndirizziComponent', () => {
  let component: IndirizziComponent;
  let fixture: ComponentFixture<IndirizziComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [IndirizziComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(IndirizziComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
