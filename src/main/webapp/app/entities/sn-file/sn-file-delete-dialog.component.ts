import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SnFile } from './sn-file.model';
import { SnFilePopupService } from './sn-file-popup.service';
import { SnFileService } from './sn-file.service';

@Component({
    selector: 'jhi-sn-file-delete-dialog',
    templateUrl: './sn-file-delete-dialog.component.html'
})
export class SnFileDeleteDialogComponent {

    snFile: SnFile;

    constructor(
        private snFileService: SnFileService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.snFileService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'snFileListModification',
                content: 'Deleted an snFile'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sn-file-delete-popup',
    template: ''
})
export class SnFileDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private snFilePopupService: SnFilePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.snFilePopupService
                .open(SnFileDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
