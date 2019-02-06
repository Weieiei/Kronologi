import { Component, OnInit } from '@angular/core';
import { AdminService } from '../../../../services/admin/admin.service';
import { ActivatedRoute, Router } from '@angular/router';
import { SnackBar } from '../../../../snackbar';
import { AdminEmployeeShiftDTO } from '../../../../interfaces/shift/admin-employee-shift-dto';

@Component({
    selector: 'app-shift',
    templateUrl: './shift.component.html',
    styleUrls: ['./shift.component.scss']
})
export class ShiftComponent implements OnInit {

    columns: string[] = ['date', 'startTime', 'endTime', 'actions'];
    employeeId: number;
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

        this.getEmployeeShifts();
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

}
