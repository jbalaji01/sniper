import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { SnFileBlob } from './sn-file-blob.model';
import { SnFileBlobPopupService } from './sn-file-blob-popup.service';
import { SnFileBlobService } from './sn-file-blob.service';
import { SnFile, SnFileService } from '../sn-file';

@Component({
    selector: 'jhi-sn-file-blob-dialog',
    templateUrl: './sn-file-blob-dialog.component.html'
})
export class SnFileBlobDialogComponent implements OnInit {

    snFileBlob: SnFileBlob;
    isSaving: boolean;

    snfiles: SnFile[];

    constructor(
        public activeModal: NgbActiveModal,
        private dataUtils: JhiDataUtils,
        private jhiAlertService: JhiAlertService,
        private snFileBlobService: SnFileBlobService,
        private snFileService: SnFileService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.snFileService.query()
            .subscribe((res: HttpResponse<SnFile[]>) => { this.snfiles = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }

    setFileData(event, entity, field, isImage) {
        this.dataUtils.setFileData(event, entity, field, isImage);
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.snFileBlob.id !== undefined) {
            this.subscribeToSaveResponse(
                this.snFileBlobService.update(this.snFileBlob));
        } else {
            this.subscribeToSaveResponse(
                this.snFileBlobService.create(this.snFileBlob));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<SnFileBlob>>) {
        result.subscribe((res: HttpResponse<SnFileBlob>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: SnFileBlob) {
        this.eventManager.broadcast({ name: 'snFileBlobListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSnFileById(index: number, item: SnFile) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-sn-file-blob-popup',
    template: ''
})
export class SnFileBlobPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private snFileBlobPopupService: SnFileBlobPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.snFileBlobPopupService
                    .open(SnFileBlobDialogComponent as Component, params['id']);
            } else {
                this.snFileBlobPopupService
                    .open(SnFileBlobDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
