import { NgModule } from '@angular/core';

import {
    MatButtonModule, MatCheckboxModule, MatIconModule, MatInputModule, MatListModule,
    MatMenuModule, MatNativeDateModule, MatProgressBarModule, MatSelectModule, MatTableModule, MatSidenavModule
} from '@angular/material';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatStepperModule } from '@angular/material/stepper';
import { CdkStepperModule } from '@angular/cdk/stepper';
import { MatDialogModule } from '@angular/material/dialog';

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
        MatSidenavModule
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
        MatSidenavModule
    ]
})
export class MaterialModule {
}
