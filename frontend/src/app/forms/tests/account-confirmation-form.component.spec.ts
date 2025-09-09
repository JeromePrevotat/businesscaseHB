import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountConfirmationFormComponent } from '../account-confirmation-form/account-confirmation-form.component';

describe('AccountConfirmationFormComponent', () => {
  let component: AccountConfirmationFormComponent;
  let fixture: ComponentFixture<AccountConfirmationFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountConfirmationFormComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountConfirmationFormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
