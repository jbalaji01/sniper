import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Patient } from './patient.model';
import { PatientService } from './patient.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-patient',
    templateUrl: './patient.component.html'
})
export class PatientComponent implements OnInit, OnDestroy {
patients: Patient[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private patientService: PatientService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.patientService.query().subscribe(
            (res: HttpResponse<Patient[]>) => {
                this.patients = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInPatients();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Patient) {
        return item.id;
    }
    registerChangeInPatients() {
        this.eventSubscriber = this.eventManager.subscribe('patientListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
