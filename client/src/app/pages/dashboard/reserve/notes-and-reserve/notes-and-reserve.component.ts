import { Component, EventEmitter, OnInit, Output } from '@angular/core';

@Component({
    selector: 'app-notes-and-reserve',
    templateUrl: './notes-and-reserve.component.html',
    styleUrls: ['./notes-and-reserve.component.scss']
})
export class NotesAndReserveComponent implements OnInit {

    notes: string;

    @Output() noteChange = new EventEmitter();

    constructor() {
    }

    ngOnInit() {
    }

    setNotesAndReserve() {
        this.noteChange.emit(this.notes);
    }

}
