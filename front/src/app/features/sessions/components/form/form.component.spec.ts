import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';

let component: FormComponent;
let fixture: ComponentFixture<FormComponent>;

const mockActivatedRoute = {
  snapshot: {
    paramMap: {
      get: jest.fn().mockReturnValue('1')
    }
  }
};

const mockSessionService = {
  sessionInformation: {
    admin: true
  }
}

const mockMatSnackBar = {
  open: jest.fn()
};

const mockSessionApiService = {
  detail: jest.fn().mockReturnValue(of({
    id: '1',
    name: 'Test Session',
    date: new Date().toISOString(),
    teacher_id: '123',
    description: 'Test Description'
  })),
  create: jest.fn().mockReturnValue(of({
    id: '1',
    name: 'New Session',
    date: new Date().toISOString(),
    teacher_id: '123',
    description: 'New Description'
  })),
  update: jest.fn().mockReturnValue(of({
    id: '1',
    name: 'Updated Session',
    date: new Date().toISOString(),
    teacher_id: '123',
    description: 'Updated Description'
  })),
};

const mockRouterCreate = {
  navigate: jest.fn(),
  url: "/session/create"
};

const mockRouterUpdate = {
  navigate: jest.fn(),
  url: "/session/update"
};

describe('FormComponentUpdate', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouterUpdate },
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

describe('FormComponentCreate', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule, 
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: ActivatedRoute, useValue: mockActivatedRoute },
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouterCreate },
      ],
      declarations: [FormComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  it("should submit session form when create", () => {
    const sessionValue = {
      name: 'New Session',
      date: '2023-08-25',
      teacher_id: '3',
      description: 'New Description'
    };

    component.sessionForm?.setValue(sessionValue);
    component.submit();

    expect(mockSessionApiService.create).toBeCalledWith(sessionValue);
    expect(mockMatSnackBar.open).toBeCalledWith('Session created !', 'Close', { duration: 3000 });
    expect(mockRouterCreate.navigate).toBeCalledWith(['sessions']);
  });
});