import {Component, OnInit} from '@angular/core';
import {VerificationService} from '../../services/verified/verification.service';
import {ActivatedRoute, Route, Router, RouterLinkActive} from "@angular/router";

@Component({
    selector: 'app-verified',
    templateUrl: './verified.component.html',
    styleUrls: ['./verified.component.scss']
})
export class VerifiedComponent implements OnInit {

    result;

    constructor(private verif: VerificationService, private route: ActivatedRoute) {
    }

    ngOnInit() {
        this.route.queryParams.subscribe(params => {
            const hash = params.hash;
            if (hash !== undefined) {
                this.verif.verify(hash).subscribe(res => {
                    console.log(res.ok);
                    this.result = res.ok;
                });
            }
        });
    }

}
