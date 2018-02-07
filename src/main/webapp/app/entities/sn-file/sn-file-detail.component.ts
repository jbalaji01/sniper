import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { SnFile } from './sn-file.model';
import { SnFileService } from './sn-file.service';

@Component({
    selector: 'jhi-sn-file-detail',
    templateUrl: './sn-file-detail.component.html'
})
export class SnFileDetailComponent implements OnInit, OnDestroy {

    snFile: SnFile;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private snFileService: SnFileService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInSnFiles();
    }

    load(id) {
        this.snFileService.find(id)
            .subscribe((snFileResponse: HttpResponse<SnFile>) => {
                this.snFile = snFileResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInSnFiles() {
        this.eventSubscriber = this.eventManager.subscribe(
            'snFileListModification',
            (response) => this.load(this.snFile.id)
        );
    }
}
