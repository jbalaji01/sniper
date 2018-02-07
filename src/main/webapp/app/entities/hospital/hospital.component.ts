import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Hospital } from './hospital.model';
import { HospitalService } from './hospital.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-hospital',
    templateUrl: './hospital.component.html'
})
export class HospitalComponent implements OnInit, OnDestroy {
hospitals: Hospital[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private hospitalService: HospitalService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.hospitalService.query().subscribe(
            (res: HttpResponse<Hospital[]>) => {
                this.hospitals = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInHospitals();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Hospital) {
        return item.id;
    }
    registerChangeInHospitals() {
        this.eventSubscriber = this.eventManager.subscribe('hospitalListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
