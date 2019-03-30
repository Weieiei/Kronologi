import { Component, Output, EventEmitter, ViewChild, OnDestroy, Input, AfterViewInit } from '@angular/core';
import { Subscription, fromEvent as observableFromEvent } from 'rxjs';
import { distinctUntilChanged, debounceTime, map } from 'rxjs/operators';

@Component({
    selector: 'app-search-input',
    templateUrl: './search-box.component.html',
    styleUrls: ['./search-box.component.scss']
})
export class SearchInputComponent implements AfterViewInit, OnDestroy {
    @Input() placeholderText: string;
    @Output() changed: EventEmitter<string> = new EventEmitter<string>();
    @ViewChild('inputWrapper') inputWrapper;
    inputSubscription: Subscription;
    searchTerm = '';

    ngAfterViewInit(): void {
        this.inputSubscription = observableFromEvent(this.inputWrapper.nativeElement, 'keyup').pipe(
            map((event: any) => {
                this.searchTerm = event.target.value;
                return event.target.value;
            }),
            debounceTime(500),
            distinctUntilChanged())
            .subscribe((value) => {
                this.changed.emit(value);
            });
    }

    ngOnDestroy(): void {
        this.inputSubscription.unsubscribe();
    }

    clearFilter($event: Event): void {
        $event.preventDefault();
        if (this.searchTerm) {
            this.searchTerm = '';
            this.changed.emit('');
        }
    }

    search($event: Event): void {
        $event.preventDefault();
        this.changed.emit(this.searchTerm);
    }
}
