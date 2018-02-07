import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { TaskHistory } from './task-history.model';
import { TaskHistoryService } from './task-history.service';

@Component({
    selector: 'jhi-task-history-detail',
    templateUrl: './task-history-detail.component.html'
})
export class TaskHistoryDetailComponent implements OnInit, OnDestroy {

    taskHistory: TaskHistory;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private taskHistoryService: TaskHistoryService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInTaskHistories();
    }

    load(id) {
        this.taskHistoryService.find(id)
            .subscribe((taskHistoryResponse: HttpResponse<TaskHistory>) => {
                this.taskHistory = taskHistoryResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInTaskHistories() {
        this.eventSubscriber = this.eventManager.subscribe(
            'taskHistoryListModification',
            (response) => this.load(this.taskHistory.id)
        );
    }
}
