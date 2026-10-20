import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AjouterCommentaireComponent } from './ajouter-commentaire.component';

describe('AjouterCommentaireComponent', () => {
  let component: AjouterCommentaireComponent;
  let fixture: ComponentFixture<AjouterCommentaireComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AjouterCommentaireComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AjouterCommentaireComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
