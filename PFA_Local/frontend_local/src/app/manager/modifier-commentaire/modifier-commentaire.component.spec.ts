import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifierCommentaireComponent } from './modifier-commentaire.component';

describe('ModifierCommentaireComponent', () => {
  let component: ModifierCommentaireComponent;
  let fixture: ComponentFixture<ModifierCommentaireComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ModifierCommentaireComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ModifierCommentaireComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
