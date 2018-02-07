import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Doctor } from './doctor.model';
import { DoctorService } from './doctor.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-doctor',
    templateUrl: './doctor.component.html'
})
export class DoctorComponent implements OnInit, OnDestroy {
doctors: Doctor[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private doctorService: DoctorService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.doctorService.query().subscribe(
            (res: HttpResponse<Doctor[]>) => {
                this.doctors = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInDoctors();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Doctor) {
        return item.id;
    }
    registerChangeInDoctors() {
        this.eventSubscriber = this.eventManager.subscribe('doctorListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
