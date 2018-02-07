import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { TaskHistory } from './task-history.model';
import { TaskHistoryService } from './task-history.service';

@Injectable()
export class TaskHistoryPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private datePipe: DatePipe,
        private modalService: NgbModal,
        private router: Router,
        private taskHistoryService: TaskHistoryService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.taskHistoryService.find(id)
                    .subscribe((taskHistoryResponse: HttpResponse<TaskHistory>) => {
                        const taskHistory: TaskHistory = taskHistoryResponse.body;
                        taskHistory.punchTime = this.datePipe
                            .transform(taskHistory.punchTime, 'yyyy-MM-ddTHH:mm:ss');
                        this.ngbModalRef = this.taskHistoryModalRef(component, taskHistory);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.taskHistoryModalRef(component, new TaskHistory());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    taskHistoryModalRef(component: Component, taskHistory: TaskHistory): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.taskHistory = taskHistory;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
