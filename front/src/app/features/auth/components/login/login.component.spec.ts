import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { LoginRequest } from '../../interfaces/loginRequest.interface';
import { of, throwError } from 'rxjs';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const mockRouter = {
    navigate: jest.fn(),
  };

  const mockAuthService = {
    login: jest.fn(),
  }

  const mockSessionService = {
    logIn: jest.fn(),
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        {provide: AuthService, useValue: mockAuthService},
        {provide: Router, useValue: mockRouter},
        {provide: SessionService, useValue: mockSessionService},
      ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should submit', () => {
    //Arrange
    const loginReq: LoginRequest = {
      email: "john.doe@test.com",
      password: "password"
    }
    component.form.setValue(loginReq);
    const sessionInfo: SessionInformation = {
      token: 'mockToken',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'John',
      lastName: 'Doe',
      admin: true
    };
    mockAuthService.login.mockReturnValue(of(sessionInfo));
    
    //Act
    component.submit();

    //Assert
    expect(mockAuthService.login).toHaveBeenCalledWith(loginReq);
    expect(mockSessionService.logIn).toHaveBeenCalledWith(sessionInfo);
    expect(mockRouter.navigate).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBe(false);
  });

  it("should not submit and set onError to true on login failure", () => {
    //Arrange
    mockAuthService.login.mockReturnValue(throwError(() => new Error('Login failed')));

    //Act
    component.submit();

    //Assert
    expect(component.onError).toBe(true);
    expect(mockAuthService.login).toHaveBeenCalledTimes(1);
    expect(mockSessionService.logIn).not.toHaveBeenCalledTimes(1);
    expect(mockRouter.navigate).not.toHaveBeenCalledTimes(1);
  });
});
