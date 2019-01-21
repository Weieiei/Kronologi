import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { AppointmentDetailed } from '../../models/appointment/AppointmentDetailed';
import { AppointmentToBook } from '../../models/appointment/AppointmentToBook';

@Injectable({
    providedIn: 'root'
})
export class AppointmentService {

    constructor(private http: HttpClient) {
    }

    public getMyAppointments(): Observable<any> {
        // return this.http.get(['api', 'user', 'appointments'].join('/'));
        return of([
            {
                'createdAt': '2019-01-08T18:34:50.385+0000',
                'updatedAt': '2019-01-08T18:34:50.385+0000',
                'id': 1,
                'client': {
                    'createdAt': '2019-01-08T18:34:50.072+0000',
                    'updatedAt': '2019-01-08T18:34:50.072+0000',
                    'id': 1,
                    'firstName': 'Admin',
                    'lastName': 'User',
                    'email': 'admin@admin.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.115+0000',
                            'updatedAt': '2019-01-08T18:34:50.115+0000',
                            'id': 2,
                            'role': 'ADMIN'
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.098+0000',
                            'updatedAt': '2019-01-08T18:34:50.098+0000',
                            'id': 1,
                            'role': 'CLIENT'
                        }
                    ],
                    'employeeServices': [],
                    'fullName': 'Admin User'
                },
                'employee': {
                    'createdAt': '2019-01-08T18:34:50.310+0000',
                    'updatedAt': '2019-01-08T18:34:50.310+0000',
                    'id': 4,
                    'firstName': 'Employee',
                    'lastName': 'User',
                    'email': 'employee@employee.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.312+0000',
                            'updatedAt': '2019-01-08T18:34:50.312+0000',
                            'id': 3,
                            'role': 'EMPLOYEE'
                        }
                    ],
                    'employeeServices': [
                        {
                            'createdAt': '2019-01-08T18:34:50.147+0000',
                            'updatedAt': '2019-01-08T18:34:50.147+0000',
                            'id': 1,
                            'name': 'BACK TO PURE LIFE',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.151+0000',
                            'updatedAt': '2019-01-08T18:34:50.151+0000',
                            'id': 2,
                            'name': 'QUICK VISIT TO THE SPA',
                            'duration': 100
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.157+0000',
                            'updatedAt': '2019-01-08T18:34:50.157+0000',
                            'id': 4,
                            'name': 'MEC EXTRA',
                            'duration': 200
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.167+0000',
                            'updatedAt': '2019-01-08T18:34:50.167+0000',
                            'id': 7,
                            'name': 'SERENITY',
                            'duration': 140,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.175+0000',
                            'updatedAt': '2019-01-08T18:34:50.175+0000',
                            'id': 9,
                            'name': 'DIVINE RELAXATION',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.186+0000',
                            'updatedAt': '2019-01-08T18:34:50.186+0000',
                            'id': 12,
                            'name': 'POETRY FOR TWO',
                            'duration': 180,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        }
                    ],
                    'fullName': 'Employee User'
                },
                'service': {
                    'createdAt': '2019-01-08T18:34:50.186+0000',
                    'updatedAt': '2019-01-08T18:34:50.186+0000',
                    'id': 12,
                    'name': 'POETRY FOR TWO',
                    'duration': 180
                },
                'date': '2019-11-30',
                'startTime': '12:00:00',
                'endTime': '15:00:00',
                'notes': 'Some note',
                'status': 'confirmed',
                'startDateTime': '2019-11-30T12:00:00',
                'endDateTime': '2019-11-30T15:00:00'
            },
            {
                'createdAt': '2019-01-08T18:34:50.389+0000',
                'updatedAt': '2019-01-08T18:34:50.389+0000',
                'id': 2,
                'client': {
                    'createdAt': '2019-01-08T18:34:50.119+0000',
                    'updatedAt': '2019-01-08T18:34:50.119+0000',
                    'id': 2,
                    'firstName': 'John',
                    'lastName': 'Doe',
                    'email': 'johndoe@johndoe.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.098+0000',
                            'updatedAt': '2019-01-08T18:34:50.098+0000',
                            'id': 1,
                            'role': 'CLIENT'
                        }
                    ],
                    'employeeServices': [],
                    'fullName': 'John Doe'
                },
                'employee': {
                    'createdAt': '2019-01-08T18:34:50.310+0000',
                    'updatedAt': '2019-01-08T18:34:50.310+0000',
                    'id': 4,
                    'firstName': 'Employee',
                    'lastName': 'User',
                    'email': 'employee@employee.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.312+0000',
                            'updatedAt': '2019-01-08T18:34:50.312+0000',
                            'id': 3,
                            'role': 'EMPLOYEE'
                        }
                    ],
                    'employeeServices': [
                        {
                            'createdAt': '2019-01-08T18:34:50.147+0000',
                            'updatedAt': '2019-01-08T18:34:50.147+0000',
                            'id': 1,
                            'name': 'BACK TO PURE LIFE',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.151+0000',
                            'updatedAt': '2019-01-08T18:34:50.151+0000',
                            'id': 2,
                            'name': 'QUICK VISIT TO THE SPA',
                            'duration': 100
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.157+0000',
                            'updatedAt': '2019-01-08T18:34:50.157+0000',
                            'id': 4,
                            'name': 'MEC EXTRA',
                            'duration': 200
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.167+0000',
                            'updatedAt': '2019-01-08T18:34:50.167+0000',
                            'id': 7,
                            'name': 'SERENITY',
                            'duration': 140,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.175+0000',
                            'updatedAt': '2019-01-08T18:34:50.175+0000',
                            'id': 9,
                            'name': 'DIVINE RELAXATION',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.186+0000',
                            'updatedAt': '2019-01-08T18:34:50.186+0000',
                            'id': 12,
                            'name': 'POETRY FOR TWO',
                            'duration': 180,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        }
                    ],
                    'fullName': 'Employee User'
                },
                'service': {
                    'createdAt': '2019-01-08T18:34:50.167+0000',
                    'updatedAt': '2019-01-08T18:34:50.167+0000',
                    'id': 7,
                    'name': 'SERENITY',
                    'duration': 140
                },
                'date': '2019-12-02',
                'startTime': '12:00:00',
                'endTime': '14:20:00',
                'notes': 'Some note',
                'status': 'confirmed',
                'startDateTime': '2019-12-02T12:00:00',
                'endDateTime': '2019-12-02T14:20:00'
            },
            {
                'createdAt': '2019-01-08T18:34:50.389+0000',
                'updatedAt': '2019-01-08T18:34:50.389+0000',
                'id': 2,
                'client': {
                    'createdAt': '2019-01-08T18:34:50.119+0000',
                    'updatedAt': '2019-01-08T18:34:50.119+0000',
                    'id': 2,
                    'firstName': 'John',
                    'lastName': 'Doe',
                    'email': 'johndoe@johndoe.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.098+0000',
                            'updatedAt': '2019-01-08T18:34:50.098+0000',
                            'id': 1,
                            'role': 'CLIENT'
                        }
                    ],
                    'employeeServices': [],
                    'fullName': 'John Doe'
                },
                'employee': {
                    'createdAt': '2019-01-08T18:34:50.310+0000',
                    'updatedAt': '2019-01-08T18:34:50.310+0000',
                    'id': 4,
                    'firstName': 'Employee',
                    'lastName': 'User',
                    'email': 'employee@employee.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.312+0000',
                            'updatedAt': '2019-01-08T18:34:50.312+0000',
                            'id': 3,
                            'role': 'EMPLOYEE'
                        }
                    ],
                    'employeeServices': [
                        {
                            'createdAt': '2019-01-08T18:34:50.147+0000',
                            'updatedAt': '2019-01-08T18:34:50.147+0000',
                            'id': 1,
                            'name': 'BACK TO PURE LIFE',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.151+0000',
                            'updatedAt': '2019-01-08T18:34:50.151+0000',
                            'id': 2,
                            'name': 'QUICK VISIT TO THE SPA',
                            'duration': 100
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.157+0000',
                            'updatedAt': '2019-01-08T18:34:50.157+0000',
                            'id': 4,
                            'name': 'MEC EXTRA',
                            'duration': 200
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.167+0000',
                            'updatedAt': '2019-01-08T18:34:50.167+0000',
                            'id': 7,
                            'name': 'SERENITY',
                            'duration': 140,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.175+0000',
                            'updatedAt': '2019-01-08T18:34:50.175+0000',
                            'id': 9,
                            'name': 'DIVINE RELAXATION',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.186+0000',
                            'updatedAt': '2019-01-08T18:34:50.186+0000',
                            'id': 12,
                            'name': 'POETRY FOR TWO',
                            'duration': 180,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        }
                    ],
                    'fullName': 'Employee User'
                },
                'service': {
                    'createdAt': '2019-01-08T18:34:50.167+0000',
                    'updatedAt': '2019-01-08T18:34:50.167+0000',
                    'id': 7,
                    'name': 'SERENITY',
                    'duration': 140
                },
                'date': '2019-12-02',
                'startTime': '12:00:00',
                'endTime': '14:20:00',
                'notes': 'Some note',
                'status': 'confirmed',
                'startDateTime': '2019-12-02T12:00:00',
                'endDateTime': '2019-12-02T14:20:00'
            },
            {
                'createdAt': '2019-01-08T18:34:50.385+0000',
                'updatedAt': '2019-01-08T18:34:50.385+0000',
                'id': 1,
                'client': {
                    'createdAt': '2019-01-08T18:34:50.072+0000',
                    'updatedAt': '2019-01-08T18:34:50.072+0000',
                    'id': 1,
                    'firstName': 'Admin',
                    'lastName': 'User',
                    'email': 'admin@admin.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.115+0000',
                            'updatedAt': '2019-01-08T18:34:50.115+0000',
                            'id': 2,
                            'role': 'ADMIN'
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.098+0000',
                            'updatedAt': '2019-01-08T18:34:50.098+0000',
                            'id': 1,
                            'role': 'CLIENT'
                        }
                    ],
                    'employeeServices': [],
                    'fullName': 'Admin User'
                },
                'employee': {
                    'createdAt': '2019-01-08T18:34:50.310+0000',
                    'updatedAt': '2019-01-08T18:34:50.310+0000',
                    'id': 4,
                    'firstName': 'Employee',
                    'lastName': 'User',
                    'email': 'employee@employee.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.312+0000',
                            'updatedAt': '2019-01-08T18:34:50.312+0000',
                            'id': 3,
                            'role': 'EMPLOYEE'
                        }
                    ],
                    'employeeServices': [
                        {
                            'createdAt': '2019-01-08T18:34:50.147+0000',
                            'updatedAt': '2019-01-08T18:34:50.147+0000',
                            'id': 1,
                            'name': 'BACK TO PURE LIFE',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.151+0000',
                            'updatedAt': '2019-01-08T18:34:50.151+0000',
                            'id': 2,
                            'name': 'QUICK VISIT TO THE SPA',
                            'duration': 100
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.157+0000',
                            'updatedAt': '2019-01-08T18:34:50.157+0000',
                            'id': 4,
                            'name': 'MEC EXTRA',
                            'duration': 200
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.167+0000',
                            'updatedAt': '2019-01-08T18:34:50.167+0000',
                            'id': 7,
                            'name': 'SERENITY',
                            'duration': 140,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.175+0000',
                            'updatedAt': '2019-01-08T18:34:50.175+0000',
                            'id': 9,
                            'name': 'DIVINE RELAXATION',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.186+0000',
                            'updatedAt': '2019-01-08T18:34:50.186+0000',
                            'id': 12,
                            'name': 'POETRY FOR TWO',
                            'duration': 180,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        }
                    ],
                    'fullName': 'Employee User'
                },
                'service': {
                    'createdAt': '2019-01-08T18:34:50.186+0000',
                    'updatedAt': '2019-01-08T18:34:50.186+0000',
                    'id': 12,
                    'name': 'POETRY FOR TWO',
                    'duration': 180
                },
                'date': '2019-11-30',
                'startTime': '12:00:00',
                'endTime': '15:00:00',
                'notes': 'Some note',
                'status': 'confirmed',
                'startDateTime': '2019-11-30T12:00:00',
                'endDateTime': '2019-11-30T15:00:00'
            },
            {
                'createdAt': '2019-01-08T18:34:50.389+0000',
                'updatedAt': '2019-01-08T18:34:50.389+0000',
                'id': 2,
                'client': {
                    'createdAt': '2019-01-08T18:34:50.119+0000',
                    'updatedAt': '2019-01-08T18:34:50.119+0000',
                    'id': 2,
                    'firstName': 'John',
                    'lastName': 'Doe',
                    'email': 'johndoe@johndoe.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.098+0000',
                            'updatedAt': '2019-01-08T18:34:50.098+0000',
                            'id': 1,
                            'role': 'CLIENT'
                        }
                    ],
                    'employeeServices': [],
                    'fullName': 'John Doe'
                },
                'employee': {
                    'createdAt': '2019-01-08T18:34:50.310+0000',
                    'updatedAt': '2019-01-08T18:34:50.310+0000',
                    'id': 4,
                    'firstName': 'Employee',
                    'lastName': 'User',
                    'email': 'employee@employee.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.312+0000',
                            'updatedAt': '2019-01-08T18:34:50.312+0000',
                            'id': 3,
                            'role': 'EMPLOYEE'
                        }
                    ],
                    'employeeServices': [
                        {
                            'createdAt': '2019-01-08T18:34:50.147+0000',
                            'updatedAt': '2019-01-08T18:34:50.147+0000',
                            'id': 1,
                            'name': 'BACK TO PURE LIFE',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.151+0000',
                            'updatedAt': '2019-01-08T18:34:50.151+0000',
                            'id': 2,
                            'name': 'QUICK VISIT TO THE SPA',
                            'duration': 100
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.157+0000',
                            'updatedAt': '2019-01-08T18:34:50.157+0000',
                            'id': 4,
                            'name': 'MEC EXTRA',
                            'duration': 200
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.167+0000',
                            'updatedAt': '2019-01-08T18:34:50.167+0000',
                            'id': 7,
                            'name': 'SERENITY',
                            'duration': 140,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.175+0000',
                            'updatedAt': '2019-01-08T18:34:50.175+0000',
                            'id': 9,
                            'name': 'DIVINE RELAXATION',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.186+0000',
                            'updatedAt': '2019-01-08T18:34:50.186+0000',
                            'id': 12,
                            'name': 'POETRY FOR TWO',
                            'duration': 180,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        }
                    ],
                    'fullName': 'Employee User'
                },
                'service': {
                    'createdAt': '2019-01-08T18:34:50.167+0000',
                    'updatedAt': '2019-01-08T18:34:50.167+0000',
                    'id': 7,
                    'name': 'SERENITY',
                    'duration': 140
                },
                'date': '2019-12-02',
                'startTime': '12:00:00',
                'endTime': '14:20:00',
                'notes': 'Some note',
                'status': 'confirmed',
                'startDateTime': '2019-12-02T12:00:00',
                'endDateTime': '2019-12-02T14:20:00'
            },
            {
                'createdAt': '2019-01-08T18:34:50.389+0000',
                'updatedAt': '2019-01-08T18:34:50.389+0000',
                'id': 3,
                'client': {
                    'createdAt': '2019-01-08T18:34:50.119+0000',
                    'updatedAt': '2019-01-08T18:34:50.119+0000',
                    'id': 2,
                    'firstName': 'John',
                    'lastName': 'Doe',
                    'email': 'johndoe@johndoe.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.098+0000',
                            'updatedAt': '2019-01-08T18:34:50.098+0000',
                            'id': 1,
                            'role': 'CLIENT'
                        }
                    ],
                    'employeeServices': [],
                    'fullName': 'John Doe'
                },
                'employee': {
                    'createdAt': '2019-01-08T18:34:50.310+0000',
                    'updatedAt': '2019-01-08T18:34:50.310+0000',
                    'id': 4,
                    'firstName': 'Employee',
                    'lastName': 'User',
                    'email': 'employee@employee.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.312+0000',
                            'updatedAt': '2019-01-08T18:34:50.312+0000',
                            'id': 3,
                            'role': 'EMPLOYEE'
                        }
                    ],
                    'employeeServices': [
                        {
                            'createdAt': '2019-01-08T18:34:50.147+0000',
                            'updatedAt': '2019-01-08T18:34:50.147+0000',
                            'id': 1,
                            'name': 'BACK TO PURE LIFE',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.151+0000',
                            'updatedAt': '2019-01-08T18:34:50.151+0000',
                            'id': 2,
                            'name': 'QUICK VISIT TO THE SPA',
                            'duration': 100
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.157+0000',
                            'updatedAt': '2019-01-08T18:34:50.157+0000',
                            'id': 4,
                            'name': 'MEC EXTRA',
                            'duration': 200
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.167+0000',
                            'updatedAt': '2019-01-08T18:34:50.167+0000',
                            'id': 7,
                            'name': 'SERENITY',
                            'duration': 140,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.175+0000',
                            'updatedAt': '2019-01-08T18:34:50.175+0000',
                            'id': 9,
                            'name': 'DIVINE RELAXATION',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.186+0000',
                            'updatedAt': '2019-01-08T18:34:50.186+0000',
                            'id': 12,
                            'name': 'POETRY FOR TWO',
                            'duration': 180,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        }
                    ],
                    'fullName': 'Employee User'
                },
                'service': {
                    'createdAt': '2019-01-09T18:34:50.167+0000',
                    'updatedAt': '2019-01-09T18:34:50.167+0000',
                    'id': 7,
                    'name': 'DONE',
                    'duration': 140
                },
                'date': '2019-01-02',
                'startTime': '12:00:00',
                'endTime': '14:20:00',
                'notes': 'Some note',
                'status': 'confirmed',
                'startDateTime': '2019-12-02T12:00:00',
                'endDateTime': '2019-12-02T14:20:00'
            },
            {
                'createdAt': '2019-01-08T18:34:50.389+0000',
                'updatedAt': '2019-01-08T18:34:50.389+0000',
                'id': 3,
                'client': {
                    'createdAt': '2019-01-08T18:34:50.119+0000',
                    'updatedAt': '2019-01-08T18:34:50.119+0000',
                    'id': 2,
                    'firstName': 'John',
                    'lastName': 'Doe',
                    'email': 'johndoe@johndoe.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.098+0000',
                            'updatedAt': '2019-01-08T18:34:50.098+0000',
                            'id': 1,
                            'role': 'CLIENT'
                        }
                    ],
                    'employeeServices': [],
                    'fullName': 'John Doe'
                },
                'employee': {
                    'createdAt': '2019-01-08T18:34:50.310+0000',
                    'updatedAt': '2019-01-08T18:34:50.310+0000',
                    'id': 4,
                    'firstName': 'Employee',
                    'lastName': 'User',
                    'email': 'employee@employee.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.312+0000',
                            'updatedAt': '2019-01-08T18:34:50.312+0000',
                            'id': 3,
                            'role': 'EMPLOYEE'
                        }
                    ],
                    'employeeServices': [
                        {
                            'createdAt': '2019-01-08T18:34:50.147+0000',
                            'updatedAt': '2019-01-08T18:34:50.147+0000',
                            'id': 1,
                            'name': 'BACK TO PURE LIFE',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.151+0000',
                            'updatedAt': '2019-01-08T18:34:50.151+0000',
                            'id': 2,
                            'name': 'QUICK VISIT TO THE SPA',
                            'duration': 100
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.157+0000',
                            'updatedAt': '2019-01-08T18:34:50.157+0000',
                            'id': 4,
                            'name': 'MEC EXTRA',
                            'duration': 200
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.167+0000',
                            'updatedAt': '2019-01-08T18:34:50.167+0000',
                            'id': 7,
                            'name': 'SERENITY',
                            'duration': 140,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.175+0000',
                            'updatedAt': '2019-01-08T18:34:50.175+0000',
                            'id': 9,
                            'name': 'DIVINE RELAXATION',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.186+0000',
                            'updatedAt': '2019-01-08T18:34:50.186+0000',
                            'id': 12,
                            'name': 'POETRY FOR TWO',
                            'duration': 180,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        }
                    ],
                    'fullName': 'Employee User'
                },
                'service': {
                    'createdAt': '2019-01-09T18:34:50.167+0000',
                    'updatedAt': '2019-01-09T18:34:50.167+0000',
                    'id': 7,
                    'name': 'DONE',
                    'duration': 140
                },
                'date': '2019-01-02',
                'startTime': '12:00:00',
                'endTime': '14:20:00',
                'notes': 'Some note',
                'status': 'confirmed',
                'startDateTime': '2019-12-02T12:00:00',
                'endDateTime': '2019-12-02T14:20:00'
            },
            {
                'createdAt': '2019-01-08T18:34:50.389+0000',
                'updatedAt': '2019-01-08T18:34:50.389+0000',
                'id': 3,
                'client': {
                    'createdAt': '2019-01-08T18:34:50.119+0000',
                    'updatedAt': '2019-01-08T18:34:50.119+0000',
                    'id': 2,
                    'firstName': 'John',
                    'lastName': 'Doe',
                    'email': 'johndoe@johndoe.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.098+0000',
                            'updatedAt': '2019-01-08T18:34:50.098+0000',
                            'id': 1,
                            'role': 'CLIENT'
                        }
                    ],
                    'employeeServices': [],
                    'fullName': 'John Doe'
                },
                'employee': {
                    'createdAt': '2019-01-08T18:34:50.310+0000',
                    'updatedAt': '2019-01-08T18:34:50.310+0000',
                    'id': 4,
                    'firstName': 'Employee',
                    'lastName': 'User',
                    'email': 'employee@employee.com',
                    'roles': [
                        {
                            'createdAt': '2019-01-08T18:34:50.312+0000',
                            'updatedAt': '2019-01-08T18:34:50.312+0000',
                            'id': 3,
                            'role': 'EMPLOYEE'
                        }
                    ],
                    'employeeServices': [
                        {
                            'createdAt': '2019-01-08T18:34:50.147+0000',
                            'updatedAt': '2019-01-08T18:34:50.147+0000',
                            'id': 1,
                            'name': 'BACK TO PURE LIFE',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.151+0000',
                            'updatedAt': '2019-01-08T18:34:50.151+0000',
                            'id': 2,
                            'name': 'QUICK VISIT TO THE SPA',
                            'duration': 100
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.157+0000',
                            'updatedAt': '2019-01-08T18:34:50.157+0000',
                            'id': 4,
                            'name': 'MEC EXTRA',
                            'duration': 200
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.167+0000',
                            'updatedAt': '2019-01-08T18:34:50.167+0000',
                            'id': 7,
                            'name': 'SERENITY',
                            'duration': 140,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.175+0000',
                            'updatedAt': '2019-01-08T18:34:50.175+0000',
                            'id': 9,
                            'name': 'DIVINE RELAXATION',
                            'duration': 150
                        },
                        {
                            'createdAt': '2019-01-08T18:34:50.186+0000',
                            'updatedAt': '2019-01-08T18:34:50.186+0000',
                            'id': 12,
                            'name': 'POETRY FOR TWO',
                            'duration': 180,
                            'handler': {},
                            'hibernateLazyInitializer': {}
                        }
                    ],
                    'fullName': 'Employee User'
                },
                'service': {
                    'createdAt': '2019-01-09T18:34:50.167+0000',
                    'updatedAt': '2019-01-09T18:34:50.167+0000',
                    'id': 7,
                    'name': 'DONE',
                    'duration': 140
                },
                'date': '2019-01-02',
                'startTime': '12:00:00',
                'endTime': '14:20:00',
                'notes': 'Some note',
                'status': 'confirmed',
                'startDateTime': '2019-12-02T12:00:00',
                'endDateTime': '2019-12-02T14:20:00'
            }
        ]);
    }

    public getAllAppointments(): Observable<AppointmentDetailed[]> {
        return this.http.get<AppointmentDetailed[]>(['api', 'admin', 'appointments'].join('/'));
    }

    public reserveAppointment(appointment: AppointmentToBook): Observable<any> {
        return this.http.post<AppointmentToBook>(['api', 'user', 'appointments'].join('/'), appointment);
    }
}
