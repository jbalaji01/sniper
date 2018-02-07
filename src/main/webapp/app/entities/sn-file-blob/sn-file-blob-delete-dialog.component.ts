import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { SnFileBlob } from './sn-file-blob.model';
import { SnFileBlobPopupService } from './sn-file-blob-popup.service';
import { SnFileBlobService } from './sn-file-blob.service';

@Component({
    selector: 'jhi-sn-file-blob-delete-dialog',
    templateUrl: './sn-file-blob-delete-dialog.component.html'
})
export class SnFileBlobDeleteDialogComponent {

    snFileBlob: SnFileBlob;

    constructor(
        private snFileBlobService: SnFileBlobService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.snFileBlobService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'snFileBlobListModification',
                content: 'Deleted an snFileBlob'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-sn-file-blob-delete-popup',
    template: ''
})
export class SnFileBlobDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private snFileBlobPopupService: SnFileBlobPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.snFileBlobPopupService
                .open(SnFileBlobDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
