import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Hospital } from './hospital.model';
import { HospitalPopupService } from './hospital-popup.service';
import { HospitalService } from './hospital.service';

@Component({
    selector: 'jhi-hospital-delete-dialog',
    templateUrl: './hospital-delete-dialog.component.html'
})
export class HospitalDeleteDialogComponent {

    hospital: Hospital;

    constructor(
        private hospitalService: HospitalService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.hospitalService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'hospitalListModification',
                content: 'Deleted an hospital'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-hospital-delete-popup',
    template: ''
})
export class HospitalDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private hospitalPopupService: HospitalPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.hospitalPopupService
                .open(HospitalDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
