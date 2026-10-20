import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddInscriptionComponent } from './add-inscription.component';

describe('AddInscriptionComponent', () => {
  let component: AddInscriptionComponent;
  let fixture: ComponentFixture<AddInscriptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AddInscriptionComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddInscriptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
