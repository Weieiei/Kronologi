import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Observable, Subscription } from 'rxjs';

@Component({
    selector: 'app-notes-and-reserve',
    templateUrl: './notes-and-reserve.component.html',
    styleUrls: ['./notes-and-reserve.component.scss']
})
export class NotesAndReserveComponent implements OnInit, OnDestroy {

    notes: string;
    notesSubscription: Subscription;
    @Input() notesEvent: Observable<string>;

    @Output() noteChange = new EventEmitter();

    constructor() {
    }

    ngOnInit() {
        this.notesSubscription = this.notesEvent.subscribe(res => this.notes = res);
    }

    ngOnDestroy() {
        this.notesSubscription.unsubscribe();
    }

    setNotesAndReserve() {
        this.noteChange.emit(this.notes);
    }

}
