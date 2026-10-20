import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsulterMonProfileComponent } from './consulter-mon-profile.component';

describe('ConsulterMonProfileComponent', () => {
  let component: ConsulterMonProfileComponent;
  let fixture: ComponentFixture<ConsulterMonProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ConsulterMonProfileComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConsulterMonProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
