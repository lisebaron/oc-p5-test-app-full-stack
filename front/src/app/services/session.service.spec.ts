import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toBeUndefined();
  });

  it("should log in and update the state correctly", () => {
    //Arrange
    const sessionInfo: SessionInformation = {
      token: 'mockToken',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'John',
      lastName: 'Doe',
      admin: true
    };

    //Act
    service.logIn(sessionInfo);

    //Assert
    expect(service.sessionInformation).toEqual(sessionInfo);
    expect(service.isLogged).toBe(true);

    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(true);
    });
  });

  it("should log off and update the state correctly", () => {
    const sessionInfo: SessionInformation = {
      token: 'mockToken',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'John',
      lastName: 'Doe',
      admin: true
    };
    service.logIn(sessionInfo);

    service.logOut();

    expect(service.sessionInformation).toBeUndefined();
    expect(service.isLogged).toBe(false);

    service.$isLogged().subscribe(isLogged => {
      expect(isLogged).toBe(true);
    });
  });
});
