import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Hospital } from './hospital.model';
import { HospitalService } from './hospital.service';

@Component({
    selector: 'jhi-hospital-detail',
    templateUrl: './hospital-detail.component.html'
})
export class HospitalDetailComponent implements OnInit, OnDestroy {

    hospital: Hospital;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private hospitalService: HospitalService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInHospitals();
    }

    load(id) {
        this.hospitalService.find(id)
            .subscribe((hospitalResponse: HttpResponse<Hospital>) => {
                this.hospital = hospitalResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInHospitals() {
        this.eventSubscriber = this.eventManager.subscribe(
            'hospitalListModification',
            (response) => this.load(this.hospital.id)
        );
    }
}
