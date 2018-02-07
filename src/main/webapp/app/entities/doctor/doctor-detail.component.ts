import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { Doctor } from './doctor.model';
import { DoctorService } from './doctor.service';

@Component({
    selector: 'jhi-doctor-detail',
    templateUrl: './doctor-detail.component.html'
})
export class DoctorDetailComponent implements OnInit, OnDestroy {

    doctor: Doctor;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private doctorService: DoctorService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInDoctors();
    }

    load(id) {
        this.doctorService.find(id)
            .subscribe((doctorResponse: HttpResponse<Doctor>) => {
                this.doctor = doctorResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInDoctors() {
        this.eventSubscriber = this.eventManager.subscribe(
            'doctorListModification',
            (response) => this.load(this.doctor.id)
        );
    }
}
