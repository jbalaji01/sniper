import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TaskHistory } from './task-history.model';
import { TaskHistoryService } from './task-history.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-task-history',
    templateUrl: './task-history.component.html'
})
export class TaskHistoryComponent implements OnInit, OnDestroy {
taskHistories: TaskHistory[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private taskHistoryService: TaskHistoryService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.taskHistoryService.query().subscribe(
            (res: HttpResponse<TaskHistory[]>) => {
                this.taskHistories = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInTaskHistories();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: TaskHistory) {
        return item.id;
    }
    registerChangeInTaskHistories() {
        this.eventSubscriber = this.eventManager.subscribe('taskHistoryListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
