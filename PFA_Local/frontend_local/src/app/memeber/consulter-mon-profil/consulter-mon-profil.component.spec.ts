import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsulterMonProfilComponent } from './consulter-mon-profil.component';

describe('ConsulterMonProfilComponent', () => {
  let component: ConsulterMonProfilComponent;
  let fixture: ComponentFixture<ConsulterMonProfilComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ConsulterMonProfilComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ConsulterMonProfilComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
