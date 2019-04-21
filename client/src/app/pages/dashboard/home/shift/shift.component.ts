import {Component, OnChanges, OnInit, SimpleChanges} from '@angular/core';
import {AdminService} from '../../../../services/admin/admin.service';
import {ActivatedRoute, Router} from '@angular/router';
import {SnackBar} from '../../../../snackbar';
import {AdminEmployeeShiftDTO} from '../../../../interfaces/shift/admin-employee-shift-dto';
import {AdminEmployeeDTO} from '../../../../interfaces/employee/admin-employee-dto';
import {HttpErrorResponse} from '@angular/common/http';
import {NewShiftDTO} from '../../../../interfaces/shift/new-shift-dto';
import {MatDialog, MatDialogConfig} from "@angular/material";
import {ErrorDialogComponent} from "../../../../components/error-dialog/error-dialog.component";

@Component({
    selector: 'app-shift',
    templateUrl: './shift.component.html',
    styleUrls: ['./shift.component.scss']
})
export class ShiftComponent implements OnInit, OnChanges {

    constructor(
        private dialog: MatDialog,
        private adminService: AdminService,
        private router: Router,
        private route: ActivatedRoute,
        private snackBar: SnackBar
    ) {
    }

    businessId: number;
    employeeId: number;
    employee: AdminEmployeeDTO;

    columns: string[] = ['date', 'startTime', 'endTime', 'actions'];
    shifts: AdminEmployeeShiftDTO[];

    showShiftform = false;
    showEditShiftform = false;

    static sortShifts(shifts: AdminEmployeeShiftDTO[]): void {
        shifts.sort((a, b) => {
            if (a.date < b.date) {
                return -1;
            } else if (a.date > b.date) {
                return 1;
            }
            return 0;
        });
    }

    ngOnInit() {
        this.businessId = parseInt(this.route.snapshot.paramMap.get("businessId"));
        this.employeeId = +this.route.snapshot.paramMap.get('id');

        if (isNaN(this.employeeId)) {
            this.openDialog('Invalid URL');
            this.router.navigate(['']);
            return;
        }

        this.getEmployee(this.businessId, this.employeeId);
        this.getEmployeeShifts(this.businessId, this.employeeId);
    }

    ngOnChanges(changes: SimpleChanges): void {
        if (isNaN(this.employeeId)) {
            this.snackBar.openSnackBarError('Invalid URL.');
            this.router.navigate(['']);
            return;
        }

        this.getEmployee(this.businessId, this.employeeId);
        this.getEmployeeShifts(this.businessId, this.employeeId);

    }

    getEmployee(businessId: number, employeeId: number) {
        this.adminService.getEmployee(businessId, employeeId).subscribe(
            res => this.employee = res,
            err => {
                if (err instanceof HttpErrorResponse) {
                    this.openDialog(err.error.message);
                    this.router.navigate(['']);
                }
            }
        );
    }

    getEmployeeShifts(businessId: number, employeeId: number) {
        this.adminService.getEmployeeShifts(businessId, employeeId).subscribe(
            res => {
                this.shifts = res;
                ShiftComponent.sortShifts(this.shifts);
            }
        );
    }



    deleteShift(businessId: number, shiftId: number) {


        if (!confirm('Click OK to delete shift')) {
            return;
        }

        this.adminService.deleteShift(businessId, shiftId).subscribe(
            res => {
                this.snackBar.openSnackBarSuccess('Successfully deleted shift.');
                this.shifts = this.shifts.filter(s => s.id !== shiftId);
            },
            err => {
                if (err instanceof HttpErrorResponse) {
                    this.openDialog(err.error.message);
                    this.snackBar.openSnackBarError(err.error.message);
                }
            }
        );
    }

    addShift(shift: NewShiftDTO) {
        this.adminService.addShift(this.businessId, this.employeeId, shift).subscribe(
            res => {
                this.snackBar.openSnackBarSuccess('Successfully added shift.');
                this.shifts.push(res);
                this.shifts = this.shifts.map(s => Object.assign({}, s));
                ShiftComponent.sortShifts(this.shifts);
                this.showShiftform = false;
            },
            err => {
                console.log(err);
                if (err instanceof HttpErrorResponse) {
                    this.openDialog(err.error.message);
                }
            }
        );
    }

    addRecurringShift(newShifts: Array<NewShiftDTO>) {
        this.adminService.addShiftList(this.businessId, this.employeeId, newShifts).subscribe(
            res => {
                this.snackBar.openSnackBarSuccess('Successfully added recurrent shift.');
                this.shifts = this.shifts.concat(res);
                this.shifts = this.shifts.map(s => Object.assign({}, s));
                ShiftComponent.sortShifts(this.shifts);
                this.showEditShiftform = false;
            },
            err => {
                console.log(err);
                if (err instanceof HttpErrorResponse) {
                  // this.openDialog(err.error.message);
                    this.snackBar.openSnackBarError(err.error.message);
                }
            }
        );
    }


    openDialog( errorMessage : any) {
        const dialogConfig = new MatDialogConfig();
        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
        dialogConfig.data = {
            title: "ERROR",
            message: errorMessage,
        };
        this.dialog.open(ErrorDialogComponent, dialogConfig);
    }


}
