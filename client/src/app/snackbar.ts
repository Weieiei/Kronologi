import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material';

@Injectable({
    providedIn: 'root'
})
export class SnackBar {

    constructor(private snackbar: MatSnackBar) {
    }

    openSnackBarSuccess(successMessage: string): void {
        this.snackbar.open(successMessage, null, { duration: 3000, panelClass: ['success-message'] });
    }

    openSnackBarError(errorMessage: string): void {
        this.snackbar.open(errorMessage, null, { duration: 3000, panelClass: ['error-message'] });
    }

}