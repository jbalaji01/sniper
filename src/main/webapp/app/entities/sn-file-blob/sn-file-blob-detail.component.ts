import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiDataUtils } from 'ng-jhipster';

import { SnFileBlob } from './sn-file-blob.model';
import { SnFileBlobService } from './sn-file-blob.service';

@Component({
    selector: 'jhi-sn-file-blob-detail',
    templateUrl: './sn-file-blob-detail.component.html'
})
export class SnFileBlobDetailComponent implements OnInit, OnDestroy {

    snFileBlob: SnFileBlob;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private dataUtils: JhiDataUtils,
        private snFileBlobService: SnFileBlobService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSnFileBlobs();
    }

    load(id) {
        this.snFileBlobService.find(id)
            .subscribe((snFileBlobResponse: HttpResponse<SnFileBlob>) => {
                this.snFileBlob = snFileBlobResponse.body;
            });
    }
    byteSize(field) {
        return this.dataUtils.byteSize(field);
    }

    openFile(contentType, field) {
        return this.dataUtils.openFile(contentType, field);
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSnFileBlobs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'snFileBlobListModification',
            (response) => this.load(this.snFileBlob.id)
        );
    }
}
