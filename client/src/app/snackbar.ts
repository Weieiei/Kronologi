import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material';

@Injectable({
    providedIn: 'root'
})
export class SnackBar {

    constructor(private snackBar: MatSnackBar) {
    }

    openSnackBarSuccess(successMessage: string, duration: number = 3000): void {
        this.snackBar.open(successMessage, null, { duration: duration, panelClass: ['success-message'] });
    }

    openSnackBarError(errorMessage: string, duration: number = 3000): void {
        this.snackBar.open(errorMessage, null, { duration: duration, panelClass: ['error-message'] });
    }

}
