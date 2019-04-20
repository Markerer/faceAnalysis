import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { HandtrackComponent } from './handtrack.component';

describe('HandtrackComponent', () => {
  let component: HandtrackComponent;
  let fixture: ComponentFixture<HandtrackComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ HandtrackComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HandtrackComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
