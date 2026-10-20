import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DashbordComponent } from './dashboard.component';

describe('DashboardComponent', () => {
  let component: DashbordComponent;
  let fixture: ComponentFixture<DashbordComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DashbordComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DashbordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
