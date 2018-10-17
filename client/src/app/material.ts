import { NgModule } from "@angular/core";
import { MatButtonModule, MatCheckboxModule, MatIconModule, MatInputModule } from '@angular/material';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatFormFieldModule } from '@angular/material/form-field';

@NgModule({
    imports: [
        MatButtonModule,
        MatCheckboxModule,
        MatToolbarModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule
    ],
    exports: [
        MatButtonModule,
        MatCheckboxModule,
        MatToolbarModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule
    ]
})
export class MaterialModule { }
