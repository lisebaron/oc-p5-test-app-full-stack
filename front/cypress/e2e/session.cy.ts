/// <reference types='Cypress' />
import { SessionInformation } from '../../src/app/interfaces/sessionInformation.interface';
import { Session } from '../../src/app/features/sessions/interfaces/session.interface.ts';
import { Teacher } from '../../src/app/interfaces/teacher.interface.ts';

//user connected participates
const sessionOne: Session = {
    id: 1,
    name: 'Session 1',
    description: 'Description of Session 1',
    date: new Date('2024-08-06'),
    teacher_id: 2,
    users: [1],
    createdAt: new Date(),
    updatedAt: new Date(),
}

//user connected does not participates
const sessionTwo: Session = {
    id: 2,
    name: 'Session 2',
    description: 'Description of Session 2',
    date: new Date('2024-08-07'),
    teacher_id: 2,
    users: [2],
    createdAt: new Date(),
    updatedAt: new Date(),
}

const teacher: Teacher = {
    id: 2,
    lastName: 'NumberTwo',
    firstName: 'Teacher',
    createdAt: new Date('2024-01-01T10:00:00.000Z'),
    updatedAt: new Date('2024-01-10T10:00:00.000Z'),
}

describe('When user is admin', () => {
    const userInfo: SessionInformation = {
        token: 'token',
        type: 'type',
        id: 1,
        username: 'username',
        firstName: 'John',
        lastName: 'Doe',
        admin: true // User is admin
    }
    
    const teacherThree: Teacher = {
        id: 3,
        lastName: 'NumberThree',
        firstName: 'Teacher',
        createdAt: new Date('2024-01-01T10:00:00.000Z'),
        updatedAt: new Date('2024-01-10T10:00:00.000Z'),
    }
    
    const newSession = {
        id: 3,
        name: 'New Session',
        date: '2024-10-09',
        teacher_id: teacher.firstName + " " + teacher.lastName,
        description: 'A Description',
        users: [],
        createdAt: new Date('2024-01-01T10:00:00.000Z'),
        updatedAt: new Date('2024-01-10T10:00:00.000Z'),
    }

    const updatedSession = {
        id: 1,
        name: 'Edit Name Session',
        date: '2025-04-21',
        teacher_id: 3,
        description: 'Edited Description',
        users: [1],
        createdAt: '2024-08-05T19:36:49',
        updatedAt: new Date('2024-01-10T10:00:00.000Z'),
    }

    before(() => {
        cy.viewport(1280, 720);
    });

    beforeEach(() => {
        cy.visit('/login');

        cy.intercept('POST', 'api/auth/login', { body: userInfo }).as('Login');
        cy.intercept({ method: 'GET', url: 'api/session' }, [ sessionOne, sessionTwo ]).as('SessionList');

        cy.get('input[formControlName=email]').type('yoga@studio.com', {force: true});
        cy.get('input[formControlName=password]').type('test!1234', {force: true});
        cy.get('button[type=submit]').click();

        cy.wait('@Login');
        cy.wait('@SessionList');
        cy.url().should('include', '/sessions');
    });

    it('Session List - should display Create and Edit buttons', () => {
        cy.get('[fxlayout="row"] > .mat-focus-indicator').should('contain', 'Create');
        cy.get(':nth-child(1) > .mat-card-actions > .ng-star-inserted').should('contain', 'Edit');
    });

    it('Session Detail - should display the button delete', () => {
        cy.intercept({ method: 'GET', url: 'api/session/1' }, sessionOne ).as('SessionOneDetails');
        cy.intercept({ method: 'GET', url: 'api/teacher/2' }, teacher ).as('TeacherDetails');
        
        // Navigate to Session detail
        cy.get(':nth-child(1) > .mat-card-actions > :nth-child(1)').click();
        cy.wait('@SessionOneDetails');
        cy.wait('@TeacherDetails');

        cy.get('.mat-button-wrapper > .ml1').should('be.visible').and("contain", "Delete");
    });

    it('Session Detail - should delete the session successfully', () => {
        cy.intercept({ method: 'GET', url: 'api/session/1' }, sessionOne ).as('SessionOneDetails');
        cy.intercept({ method: 'GET', url: 'api/teacher/2' }, teacher ).as('TeacherDetails');
        cy.intercept('DELETE', 'api/session/1', {
            statusCode: 200,
        }).as('DeleteSession');

        // Navigate to Session detail
        cy.get(':nth-child(1) > .mat-card-actions > :nth-child(1)').click();
        cy.wait('@SessionOneDetails');
        cy.wait('@TeacherDetails');

        cy.get('.mat-button-wrapper > .ml1').should('be.visible').and("contain", "Delete").click();
        cy.wait('@DeleteSession')
    });

    //#region Session Create
    it('Session Create - should access create session page', () => {
        cy.intercept({ method: 'GET', url: 'api/teacher' }, [ teacher, teacherThree ]).as('TeacherList');

        // Navigate to Session Create page
        cy.get('[fxlayout="row"] > .mat-focus-indicator').click();
        cy.wait('@TeacherList');

        cy.get('.mat-card-title > div > .ng-star-inserted').should('contain', 'Create session');
        cy.url().should('include', '/create');
    });

    it('Session Create - should create a session successfully', () => {
        cy.intercept({ method: 'GET', url: 'api/teacher' }, [ teacher, teacherThree ]).as('TeacherList');
        cy.intercept({ method: 'POST', url: 'api/session' }, newSession).as('CreateSession');
        
        // Navigate to Session Create page
        cy.get('[fxlayout="row"] > .mat-focus-indicator').click();
        cy.wait('@TeacherList');

        cy.get('input[formControlName=name]').type(newSession.name, {force: true});
        cy.get('input[formControlName=date]').type(newSession.date, {force: true});

        //Select the teacher
        cy.get('.mat-form-field-type-mat-select > .mat-form-field-wrapper > .mat-form-field-flex').click();
        cy.get('#mat-option-0').click();
        cy.get('textarea[formControlName=description]').type(newSession.description, {force: true});

        cy.get('.mt2 > [fxlayout="row"] > .mat-focus-indicator').should('be.enabled').click();
        cy.wait('@CreateSession');
        cy.get('.mat-snack-bar-container').should('be.visible');
        
        cy.url().should('include', '/sessions');
    });

    const testInputs = [
        {field: 'name'},
        {field: 'date'},
        {field: 'teacher_id'},
        {field: 'description'},
    ];

    Cypress._.each(testInputs, (testInput) => {
        it(`Session Create - should disable the save button if the ${testInput.field} is not filled`, () => {
            cy.intercept({ method: 'GET', url: 'api/teacher' }, [ teacher, teacherThree ]).as('TeacherList');
            
            // Navigate to Session Create page
            cy.get('[fxlayout="row"] > .mat-focus-indicator').click();
            cy.wait('@TeacherList');

            cy.get('input[formControlName=name]').should('be.enabled').type(newSession.name, {force: true});
            cy.get('input[formControlName=date]').should('be.enabled').type(newSession.date, {force: true});

            if (testInput.field !== 'teacher_id') {
                //Select the teacher
                cy.get('.mat-form-field-type-mat-select > .mat-form-field-wrapper > .mat-form-field-flex').click();
                cy.get('#mat-option-0').click();
            }
            
            if (testInput.field !== 'description') {
                cy.get('textarea[formControlName=description]').type(newSession.description, {force: true});
            }

            if (testInput.field === 'name' || testInput.field === 'date') {
                cy.get(`input[formControlName=${testInput.field}]`).clear();
            }

            cy.get('.mt2 > [fxlayout="row"] > .mat-focus-indicator').should('be.disabled');
        });
    });
    //#endregion

    it('Session Update - should access update session page', () => {
        cy.intercept({ method: 'GET', url: 'api/teacher' }, [ teacher, teacherThree ]).as('TeacherList');
        cy.intercept({ method: 'GET', url: 'api/session/1' }, sessionOne ).as('SessionOneDetails');
        // Navigate to Session Update page
        cy.get(':nth-child(1) > .mat-card-actions > .ng-star-inserted').click();
        cy.wait('@SessionOneDetails');
        cy.wait('@TeacherList');

        cy.get('.mat-card-title > div > .ng-star-inserted').should('contain', 'Update session');
        cy.url().should('include', '/update');
    });

    it('Session Update - should update every field of a session successfully', () => {
        cy.intercept({ method: 'GET', url: 'api/teacher' }, [ teacher, teacherThree ]).as('TeacherList');
        cy.intercept({ method: 'GET', url: 'api/session/1' }, sessionOne ).as('SessionOneDetails');
        cy.intercept({ method: 'PUT', url: 'api/session/1' }, updatedSession).as('UpdateSession');

        // Navigate to Session Update page
        cy.get(':nth-child(1) > .mat-card-actions > .ng-star-inserted').click();
        cy.wait('@SessionOneDetails');
        cy.wait('@TeacherList');

        cy.get('input[formControlName=name]').type(updatedSession.name, {force: true});
        cy.get('input[formControlName=date]').type(updatedSession.date, {force: true});

        //Select the teacher
        cy.get('.mat-form-field-type-mat-select > .mat-form-field-wrapper > .mat-form-field-flex').click();
        cy.get('#mat-option-1').click();
        cy.get('textarea[formControlName=description]').type(updatedSession.description, {force: true});

        cy.get('.mt2 > [fxlayout="row"] > .mat-focus-indicator').should('be.enabled').click();
        cy.wait('@UpdateSession');
        cy.get('.mat-snack-bar-container').should('be.visible');

        cy.url().should('include', '/sessions');
    });
});

describe('When user is not admin', () => {
    const userInfo: SessionInformation = {
        token: 'token',
        type: 'type',
        id: 1,
        username: 'username',
        firstName: 'John',
        lastName: 'Doe',
        admin: false // User is not admin
    }

    before(() => {
        cy.viewport(1280, 720);
    });

    beforeEach(() => {
        cy.visit('/login');

        cy.intercept('POST', 'api/auth/login', { body: userInfo }).as('Login');

        cy.intercept({ method: 'GET', url: 'api/session' },[ sessionOne, sessionTwo ]).as('SessionList');
        cy.intercept({ method: 'GET', url: 'api/session/1' }, sessionOne ).as('SessionOneDetails');
        cy.intercept({ method: 'GET', url: 'api/session/2' }, sessionTwo ).as('SessionTwoDetails');
        cy.intercept({ method: 'GET', url: 'api/teacher/2' }, teacher ).as('TeacherDetails');

        cy.get('input[formControlName=email]').type('yoga@studio.com', {force: true});
        cy.get('input[formControlName=password]').type('test!1234', {force: true});
        cy.get('button[type=submit]').click();

        cy.wait('@Login');
        cy.wait('@SessionList');
        cy.url().should('include', '/sessions');
    });

    it('Session List - should display the list of sessions', () => {
        cy.get('.item').should('have.length', 2).and((items) => {
            expect(items[0]).to.contain(sessionOne.name);
            expect(items[0]).to.contain(sessionOne.description);
            expect(items[1]).to.contain(sessionTwo.name);
            expect(items[1]).to.contain(sessionTwo.description);
        });
    });

    it('Session Detail - should display correctly infos on the session', () => {
        // Navigate to Session Detail
        cy.get(':nth-child(1) > .mat-card-actions > .mat-focus-indicator').click();
        cy.wait('@SessionOneDetails');
        cy.wait('@TeacherDetails');

        cy.get('h1').should('contain', 'Session 1');
        cy.get('.ml3 > .ml1').should('contain', teacher.firstName + ' ' + teacher.lastName.toUpperCase());
        cy.get('.mat-card-content > :nth-child(1) > :nth-child(1) > .ml1').should('contain', '1 attendees');
        cy.get(':nth-child(2) > .ml1').should('contain', sessionOne.date.toLocaleDateString('en-US', {year: 'numeric', month: 'long', day: 'numeric'}));
        cy.get('.description').should('contain', 'Description of Session 1')
        cy.get('.created').should('contain', sessionOne.createdAt.toLocaleDateString('en-US', {year: 'numeric', month: 'long', day: 'numeric'}));
        cy.get('.updated').should('contain', sessionOne.updatedAt.toLocaleDateString('en-US', {year: 'numeric', month: 'long', day: 'numeric'}));
    });

    it('Session Detail - should be participating', () => {
        // Navigate to Session Detail
        cy.get(':nth-child(1) > .mat-card-actions > .mat-focus-indicator').click();

        cy.wait('@SessionOneDetails');
        cy.wait('@TeacherDetails');

        cy.get('.mat-button-wrapper > .ml1').should('be.visible').and("contain", "Do not participate");
    });

    it('Session Detail - should not be participating', () => {
        // Navigate to Session Detail
        cy.get(':nth-child(2) > .mat-card-actions > .mat-focus-indicator').click();

        cy.wait('@SessionTwoDetails');
        cy.wait('@TeacherDetails');

        cy.get('.mat-button-wrapper > .ml1').should('be.visible').and("contain", "Participate");
    });

    it('Session Detail - should not see the button delete when not admin', () => {
        // Navigate to Session Detail
        cy.get(':nth-child(1) > .mat-card-actions > .mat-focus-indicator').click();

        cy.wait('@SessionOneDetails');
        cy.wait('@TeacherDetails');

        cy.get('.mat-button-wrapper > .ml1').should('be.visible').and("not.contain", "Delete");
    });
});