import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NotPageFComponent } from './not-page-f.component';

describe('NotPageFComponent', () => {
  let component: NotPageFComponent;
  let fixture: ComponentFixture<NotPageFComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [NotPageFComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(NotPageFComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
