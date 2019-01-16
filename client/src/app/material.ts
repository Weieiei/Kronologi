import { NgModule } from '@angular/core';

import {
    MatButtonModule,
    MatCardModule,
    MatCheckboxModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatProgressBarModule,
    MatSelectModule,
    MatSidenavModule,
    MatTableModule
} from '@angular/material';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatStepperModule } from '@angular/material/stepper';
import { CdkStepperModule } from '@angular/cdk/stepper';
import { MatDialogModule } from '@angular/material/dialog';
import { MatAutocompleteModule } from "@angular/material/autocomplete";

@NgModule({
    imports: [
        MatButtonModule,
        MatCheckboxModule,
        MatToolbarModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatTableModule,
        MatMenuModule,
        MatGridListModule,
        MatStepperModule,
        MatProgressBarModule,
        MatListModule,
        MatDialogModule,
        CdkStepperModule,
        MatSidenavModule,
        MatCardModule,
        MatAutocompleteModule
    ],
    exports: [
        MatButtonModule,
        MatCheckboxModule,
        MatToolbarModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatDatepickerModule,
        MatNativeDateModule,
        MatTableModule,
        MatMenuModule,
        MatGridListModule,
        MatStepperModule,
        MatProgressBarModule,
        MatListModule,
        MatDialogModule,
        CdkStepperModule,
        MatSidenavModule,
        MatCardModule,
        MatAutocompleteModule
    ]
})
export class MaterialModule {
}
