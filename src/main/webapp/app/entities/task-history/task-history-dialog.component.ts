import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { TaskHistory } from './task-history.model';
import { TaskHistoryPopupService } from './task-history-popup.service';
import { TaskHistoryService } from './task-history.service';
import { Task, TaskService } from '../task';
import { UserInfo, UserInfoService } from '../user-info';

@Component({
    selector: 'jhi-task-history-dialog',
    templateUrl: './task-history-dialog.component.html'
})
export class TaskHistoryDialogComponent implements OnInit {

    taskHistory: TaskHistory;
    isSaving: boolean;

    tasks: Task[];

    userinfos: UserInfo[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private taskHistoryService: TaskHistoryService,
        private taskService: TaskService,
        private userInfoService: UserInfoService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.taskService.query()
            .subscribe((res: HttpResponse<Task[]>) => { this.tasks = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.userInfoService.query()
            .subscribe((res: HttpResponse<UserInfo[]>) => { this.userinfos = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.taskHistory.id !== undefined) {
            this.subscribeToSaveResponse(
                this.taskHistoryService.update(this.taskHistory));
        } else {
            this.subscribeToSaveResponse(
                this.taskHistoryService.create(this.taskHistory));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<TaskHistory>>) {
        result.subscribe((res: HttpResponse<TaskHistory>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: TaskHistory) {
        this.eventManager.broadcast({ name: 'taskHistoryListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackTaskById(index: number, item: Task) {
        return item.id;
    }

    trackUserInfoById(index: number, item: UserInfo) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-task-history-popup',
    template: ''
})
export class TaskHistoryPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private taskHistoryPopupService: TaskHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.taskHistoryPopupService
                    .open(TaskHistoryDialogComponent as Component, params['id']);
            } else {
                this.taskHistoryPopupService
                    .open(TaskHistoryDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
