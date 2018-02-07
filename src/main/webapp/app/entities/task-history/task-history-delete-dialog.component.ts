import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { TaskHistory } from './task-history.model';
import { TaskHistoryPopupService } from './task-history-popup.service';
import { TaskHistoryService } from './task-history.service';

@Component({
    selector: 'jhi-task-history-delete-dialog',
    templateUrl: './task-history-delete-dialog.component.html'
})
export class TaskHistoryDeleteDialogComponent {

    taskHistory: TaskHistory;

    constructor(
        private taskHistoryService: TaskHistoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.taskHistoryService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'taskHistoryListModification',
                content: 'Deleted an taskHistory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-task-history-delete-popup',
    template: ''
})
export class TaskHistoryDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private taskHistoryPopupService: TaskHistoryPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.taskHistoryPopupService
                .open(TaskHistoryDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
