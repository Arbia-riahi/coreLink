import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListCertificationComponent } from './list-certification.component';

describe('ListCertificationComponent', () => {
  let component: ListCertificationComponent;
  let fixture: ComponentFixture<ListCertificationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListCertificationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ListCertificationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
