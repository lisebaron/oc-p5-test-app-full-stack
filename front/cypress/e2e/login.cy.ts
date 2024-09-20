/// <reference types='Cypress' />
import { SessionInformation } from '../../src/app/interfaces/sessionInformation.interface';
import { LoginRequest } from '../../src/app/features/auth/interfaces/loginRequest.interface.ts';

describe('Login spec', () => {
  const userInfo: SessionInformation = {
    token: 'token',
    type: 'type',
    id: 1,
    username: 'username',
    firstName: 'John',
    lastName: 'Doe',
    admin: false
  }

  const logReq: LoginRequest = {
    email: 'yoga@studio.com',
    password: 'test!1234'
  }

  before(() => {
    cy.viewport(1280, 720);
  });

  beforeEach(() => {
    cy.visit('/login');
  });

  it('should login successfully', () => {
    cy.intercept('POST', 'api/auth/login', { body: userInfo }).as('LoginRequest');

    cy.get('input[formControlName=email]').type(logReq.email);
    cy.get('input[formControlName=password]').type(logReq.password);
    cy.get('button[type=submit]').click();
    cy.wait('@LoginRequest');
    
    cy.url().should('include', '/sessions');
  });

  it('should not login and display error text', () => {
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
    }).as('LoginFailedRequest');

    cy.get('input[formControlName=email]').type(logReq.email);
    cy.get('input[formControlName=password]').type('notTheTruePassword');
    cy.get('button[type=submit]').click();

    cy.wait('@LoginFailedRequest');

    cy.get('.error').should('be.visible');
  });
  
  it('should logout', () => {
    cy.intercept('POST', 'api/auth/login', { body: userInfo }).as('LoginRequest');

    cy.get('input[formControlName=email]').type(logReq.email);
    cy.get('input[formControlName=password]').type(logReq.password);
    cy.get('button[type=submit]').click();
    
    cy.wait('@LoginRequest');

    cy.contains('span', 'Logout').click();
    cy.url().should('eq', `${Cypress.config().baseUrl}`);
  });

  const testCases = [
    { field: 'email', value: logReq.email },
    { field: 'password', value: logReq.password }
  ];

  Cypress._.each(testCases, (testCase) => {
    it(`should disable the submit button if the ${testCase.field} is not filled`, () => {
      cy.get('input[formControlName=email]').type(logReq.email);
      cy.get('input[formControlName=password]').type(logReq.password);
  
      cy.get(`input[formControlName=${testCase.field}]`).clear();
  
      cy.get('button[type=submit]').should('be.disabled');
    });
  });
});