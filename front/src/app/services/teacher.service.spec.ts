import { HttpClientTestingModule, HttpTestingController } from "@angular/common/http/testing";
import { TestBed } from "@angular/core/testing";
import { expect } from "@jest/globals";

import { TeacherService } from "./teacher.service";
import { Teacher } from "../interfaces/teacher.interface";

describe("TeacherService", () => {
  let service: TeacherService;

  let httpTestingController: HttpTestingController;

  const mockTeachers: Teacher[] = [
    {id: 1, lastName: "Doe", firstName: "John", createdAt: new Date("2024-01-01T00:00:00Z"), updatedAt: new Date("2024-01-01T00:00:00Z")},
    {id: 2, lastName: "Smith", firstName: "Jane", createdAt: new Date("2024-05-01T00:00:00Z"), updatedAt: new Date("2024-05-01T00:00:00Z")}
  ];
  
  const mockTeacher: Teacher = {
    id: 1, lastName: "Doe", firstName: "John", createdAt: new Date("2024-01-01T00:00:00Z"), updatedAt: new Date("2024-01-01T00:00:00Z")
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ]
    });
    httpTestingController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(TeacherService);
  });

  afterEach(() => {
    httpTestingController.verify();
  });

  it("should be created", () => {
    expect(service).toBeTruthy();
  });

  it("should fetch all teachers", () => {
    //Act
    service.all().subscribe((teachers) => {
      //Assert
      expect(teachers).toEqual(mockTeachers);
    });
    
    //Assert
    const req = httpTestingController.expectOne("api/teacher");
    expect(req.request.method).toBe("GET");
    //Act
    req.flush(mockTeachers);
  });

  it("should fetch a teacher by its Id", () => {
    let teacherId = "1";
    //Act
    service.detail(teacherId).subscribe((teacher) => {
      //Assert
      expect(teacher).toEqual(mockTeacher);
    });
    
    //Assert
    const req = httpTestingController.expectOne(`api/teacher/${teacherId}`);
    expect(req.request.method).toBe("GET");
    //Act
    req.flush(mockTeacher);
  });
});
