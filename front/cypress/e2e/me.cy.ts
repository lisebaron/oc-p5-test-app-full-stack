/// <reference types='Cypress' />
import { SessionInformation } from '../../src/app/interfaces/sessionInformation.interface';
import { User } from '../../src/app/interfaces/user.interface';

describe('User informations', () => {
    const userInfo: SessionInformation = {
        token: 'token',
        type: 'type',
        id: 1,
        username: 'username',
        firstName: 'John',
        lastName: 'Doe',
        admin: false
    }

    const user: User = {
        id: userInfo.id,
        email: 'john.doe@test.com',
        lastName: userInfo.lastName,
        firstName: userInfo.firstName,
        admin: userInfo.admin,
        password: '123456',
        createdAt: new Date(),
        updatedAt: new Date(),
    }

    before(() => {
        cy.viewport(1280, 720);
    });

    beforeEach(() => {
        cy.visit('/login');

        cy.intercept('POST', 'api/auth/login', { body: userInfo }).as('Login');
        cy.intercept({ method: 'GET', url: 'api/user/1' }, user ).as('GetUserById');

        cy.get('input[formControlName=email]').type('yoga@studio.com', {force: true});
        cy.get('input[formControlName=password]').type('test!1234', {force: true});
        cy.get('button[type=submit]').click();

        cy.wait('@Login');
        cy.url().should('include', '/sessions');
    });

    it('should display correctly the user informations', () => {
        cy.get('[routerlink="me"]').should('be.visible').click();
        cy.contains('span', 'Account').click();
        cy.wait('@GetUserById');

        cy.get('.mat-card-content > [fxlayoutalign="start center"] > :nth-child(1)').should('contain', user.firstName + ' ' + user.lastName.toUpperCase());
        cy.get('.mat-card-content > [fxlayoutalign="start center"] > :nth-child(2)').should('contain', user.email);
        cy.get('.p2 > :nth-child(1)').should('contain', user.createdAt.toLocaleDateString('en-US', {year: 'numeric', month: 'long', day: 'numeric'}));
        cy.get('.p2 > :nth-child(2)').should('contain', user.updatedAt.toLocaleDateString('en-US', {year: 'numeric', month: 'long', day: 'numeric'}));
    });

    it('should delete the user and logout', () => {
        cy.intercept('DELETE', 'api/user/1', {
          statusCode: 200,
        }).as('DeleteUser');

        cy.get('[routerlink="me"]').should('be.visible').click();
        cy.contains('span', 'Account').click();
        cy.wait('@GetUserById');
        
        //click Delete button
        cy.get('.my2 > .mat-focus-indicator').should('be.visible').click();
        cy.wait('@DeleteUser');
        cy.get('.mat-snack-bar-container').should('be.visible');
    });

    it('should go back', () => {
        cy.get('[routerlink="me"]').should('be.visible').click();
        
        cy.get('[fxlayout="row"] > .mat-focus-indicator').click();
        cy.url().should('contain', '/sessions');
    });
});