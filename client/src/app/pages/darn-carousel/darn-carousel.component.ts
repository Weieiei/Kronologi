import {Component, ViewChild} from '@angular/core';
import {MatDatepickerInputEvent, MatDialogConfig, MatTabChangeEvent, MatTabsModule} from '@angular/material';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {DialogOverviewExampleDialogComponent} from "../dialog-overview-example-dialog/dialog-overview-example-dialog.component";
import { BrowserModule } from '@angular/platform-browser';
import { SwiperModule } from 'angular2-useful-swiper';
import { NgModule } from '@angular/core';
import * as $ from 'jquery';
import {Observable, Observer} from "rxjs";
import {CancelDialogComponent} from "../../components/cancel-dialog/cancel-dialog.component";
import {SchedulerComponent} from "../dashboard/reserve/scheduler/scheduler.component";
import {ReasonDialogComponent} from "../../components/reason-dialog/reason-dialog.component";
import {MonthPickerComponent} from "../dashboard/reserve/scheduler/month-picker/month-picker.component";


export interface ExampleTab {
    label: string;
    content: string;
}


@Component({
  selector: 'darn-carousel',
  templateUrl: './darn-carousel.component.html',
  styleUrls: ['./darn-carousel.component.scss']
})


export class DarnCarouselComponent {
    label: string;
    content: string;
    asyncTabs: Observable<ExampleTab[]>;
    events: string[] = [];
    private modal: any = [];

    addEvent(type: string, event: MatDatepickerInputEvent<Date>) {
        this.events.push(`${type}: ${event.value}`);
        console.log(event);
    }
    constructor(private dialog: MatDialog){

    }
    openReasonDialog(): void {
        let dialogRef = this.dialog.open(SchedulerComponent, {
            width: '100vh',
            height:  '100vh',
            maxWidth: '100vh',
            maxHeight: '100vh',
            hasBackdrop: false
        });
    }


    open123ReasonDialog(){
        const dialogConfig = new MatDialogConfig();
        dialogConfig.width = '100vh';
        dialogConfig.height = '100vh';
        dialogConfig.maxWidth= '100vh';
        dialogConfig.maxHeight='100vh';
        dialogConfig.disableClose = true;
        dialogConfig.autoFocus = true;
        dialogConfig.data = {
        };

        this.dialog.open(SchedulerComponent, dialogConfig);
    }

    ngAfterViewInit() {
     //   console.log(this.tabGroup.selectedIndex);
    }

    public tabChanged(tabChangeEvent: MatTabChangeEvent): void {
        console.log(tabChangeEvent);
    }
}

