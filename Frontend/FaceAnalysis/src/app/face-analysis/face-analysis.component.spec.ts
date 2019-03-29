import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FaceAnalysisComponent } from './face-analysis.component';

describe('FaceAnalysisComponent', () => {
  let component: FaceAnalysisComponent;
  let fixture: ComponentFixture<FaceAnalysisComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ FaceAnalysisComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(FaceAnalysisComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
