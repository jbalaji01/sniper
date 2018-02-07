import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService, JhiDataUtils } from 'ng-jhipster';

import { SnFileBlob } from './sn-file-blob.model';
import { SnFileBlobService } from './sn-file-blob.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-sn-file-blob',
    templateUrl: './sn-file-blob.component.html'
})
export class SnFileBlobComponent implements OnInit, OnDestroy {
snFileBlobs: SnFileBlob[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private snFileBlobService: SnFileBlobService,
        private jhiAlertService: JhiAlertService,
        private dataUtils: JhiDataUtils,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.snFileBlobService.query().subscribe(
            (res: HttpResponse<SnFileBlob[]>) => {
                this.snFileBlobs = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInSnFileBlobs();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: SnFileBlob) {
        return item.id;
    }

    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    registerChangeInSnFileBlobs() {
        this.eventSubscriber = this.eventManager.subscribe('snFileBlobListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
