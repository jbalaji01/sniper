import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Hospital } from './hospital.model';
import { HospitalPopupService } from './hospital-popup.service';
import { HospitalService } from './hospital.service';
import { Doctor, DoctorService } from '../doctor';

@Component({
    selector: 'jhi-hospital-dialog',
    templateUrl: './hospital-dialog.component.html'
})
export class HospitalDialogComponent implements OnInit {

    hospital: Hospital;
    isSaving: boolean;

    doctors: Doctor[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private hospitalService: HospitalService,
        private doctorService: DoctorService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.doctorService.query()
            .subscribe((res: HttpResponse<Doctor[]>) => { this.doctors = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.hospital.id !== undefined) {
            this.subscribeToSaveResponse(
                this.hospitalService.update(this.hospital));
        } else {
            this.subscribeToSaveResponse(
                this.hospitalService.create(this.hospital));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Hospital>>) {
        result.subscribe((res: HttpResponse<Hospital>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Hospital) {
        this.eventManager.broadcast({ name: 'hospitalListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackDoctorById(index: number, item: Doctor) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-hospital-popup',
    template: ''
})
export class HospitalPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private hospitalPopupService: HospitalPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.hospitalPopupService
                    .open(HospitalDialogComponent as Component, params['id']);
            } else {
                this.hospitalPopupService
                    .open(HospitalDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
