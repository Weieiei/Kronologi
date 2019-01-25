import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material';

@Injectable({
    providedIn: 'root'
})
export class SnackBar {

    constructor(private snackBar: MatSnackBar) {
    }

    openSnackBarSuccess(successMessage: string): void {
        this.snackBar.open(successMessage, null, { duration: 3000, panelClass: ['success-message'] });
    }

    openSnackBarError(errorMessage: string): void {
        this.snackBar.open(errorMessage, null, { duration: 3000, panelClass: ['error-message'] });
    }

}
