/// <reference types='Cypress' />
import { RegisterRequest } from '../../src/app/features/auth/interfaces/registerRequest.interface.ts';

describe('Register', () => {
    const registerRequest: RegisterRequest = {
        email: 'yoga@studio.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'test!1234'
    }

    before(() => {
        cy.viewport(1280, 720);
    });

    beforeEach(() => {
        cy.visit('/register');
    });

    it('should register successfully', () => {
        cy.intercept('POST', 'api/auth/register', { body: registerRequest }).as('Register');

        cy.get('input[formControlName=firstName]').type(registerRequest.firstName);
        cy.get('input[formControlName=lastName]').type(registerRequest.lastName);
        cy.get('input[formControlName=email]').type(registerRequest.email);
        cy.get('input[formControlName=password]').type(registerRequest.password);

        cy.get('button[type=submit]').click();

        cy.wait('@Register');

        cy.url().should('include', '/login');
    });

    const testCases = [
        { field: 'firstName', value: registerRequest.firstName },
        { field: 'lastName', value: registerRequest.lastName },
        { field: 'email', value: registerRequest.email },
        { field: 'password', value: registerRequest.password }
    ];
    
    Cypress._.each(testCases, (testCase) => {
        it(`should disable the submit button if the ${testCase.field} is not filled`, () => {
            cy.get('input[formControlName=firstName]').type(registerRequest.firstName);
            cy.get('input[formControlName=lastName]').type(registerRequest.lastName);
            cy.get('input[formControlName=email]').type(registerRequest.email);
            cy.get('input[formControlName=password]').type(registerRequest.password);
        
            cy.get(`input[formControlName=${testCase.field}]`).clear();
        
            cy.get('button[type=submit]').should('be.disabled');
        });
    });
});