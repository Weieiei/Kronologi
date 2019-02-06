import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../services/admin/admin.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SnackBar } from '../../../../snackbar';
import { AdminEmployeeShiftDTO } from '../../../../interfaces/shift/admin-employee-shift-dto';
import { AdminEmployeeDTO } from '../../../../interfaces/employee/admin-employee-dto';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'app-shift',
    templateUrl: './shift.component.html',
    styleUrls: ['./shift.component.scss']
})
export class ShiftComponent implements OnInit {

    employeeId: number;
    employee: AdminEmployeeDTO;

    columns: string[] = ['date', 'startTime', 'endTime', 'actions'];
    shifts: AdminEmployeeShiftDTO[];

    constructor(
        private adminService: AdminService,
        private router: Router,
        private route: ActivatedRoute,
        private snackBar: SnackBar
    ) {
    }

    ngOnInit() {
        this.employeeId = +this.route.snapshot.paramMap.get('id');

        if (isNaN(this.employeeId)) {
            this.snackBar.openSnackBarError('Invalid URL.');
            this.router.navigate(['']);
            return;
        }

        this.getEmployee();
        this.getEmployeeShifts();
    }

    getEmployee() {
        this.adminService.getEmployee(this.employeeId).subscribe(
            res => this.employee = res,
            err => {
                if (err instanceof HttpErrorResponse) {
                    this.snackBar.openSnackBarError(err.error.message);
                    this.router.navigate(['']);
                }
            }
        );
    }

    getEmployeeShifts() {
        this.adminService.getEmployeeShifts(this.employeeId).subscribe(
            res => {
                this.shifts = res;
                this.shifts.sort((a, b) => {
                    if (a.date < b.date) {
                        return -1;
                    } else if (a.date > b.date) {
                        return 1;
                    }
                    return 0;
                });
            }
        );
    }

    deleteShift(shiftId: number) {

        if (!confirm('Click OK to delete this shift.')) {
            return;
        }

        this.adminService.deleteShift(shiftId).subscribe(
            res => {
                this.snackBar.openSnackBarSuccess('Successfully deleted shift.');
                this.shifts = this.shifts.filter(s => s.id !== shiftId);
            },
            err => {
                if (err instanceof HttpErrorResponse) {
                    this.snackBar.openSnackBarError(err.error.message);
                }
            }
        );
    }
}
