import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateCertificationComponent } from './update-certification.component';

describe('UpdateCertificationComponent', () => {
  let component: UpdateCertificationComponent;
  let fixture: ComponentFixture<UpdateCertificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [UpdateCertificationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(UpdateCertificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
