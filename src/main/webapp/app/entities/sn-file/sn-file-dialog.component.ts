import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { SnFile } from './sn-file.model';
import { SnFilePopupService } from './sn-file-popup.service';
import { SnFileService } from './sn-file.service';
import { SnFileBlob, SnFileBlobService } from '../sn-file-blob';
import { Patient, PatientService } from '../patient';
import { Task, TaskService } from '../task';
import { UserInfo, UserInfoService } from '../user-info';

@Component({
    selector: 'jhi-sn-file-dialog',
    templateUrl: './sn-file-dialog.component.html'
})
export class SnFileDialogComponent implements OnInit {

    snFile: SnFile;
    isSaving: boolean;

    snfileblobs: SnFileBlob[];

    patients: Patient[];

    tasks: Task[];

    userinfos: UserInfo[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private snFileService: SnFileService,
        private snFileBlobService: SnFileBlobService,
        private patientService: PatientService,
        private taskService: TaskService,
        private userInfoService: UserInfoService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.snFileBlobService
            .query({filter: 'snfile(filename)-is-null'})
            .subscribe((res: HttpResponse<SnFileBlob[]>) => {
                if (!this.snFile.snFileBlob || !this.snFile.snFileBlob.id) {
                    this.snfileblobs = res.body;
                } else {
                    this.snFileBlobService
                        .find(this.snFile.snFileBlob.id)
                        .subscribe((subRes: HttpResponse<SnFileBlob>) => {
                            this.snfileblobs = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.patientService.query()
            .subscribe((res: HttpResponse<Patient[]>) => { this.patients = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
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
        if (this.snFile.id !== undefined) {
            this.subscribeToSaveResponse(
                this.snFileService.update(this.snFile));
        } else {
            this.subscribeToSaveResponse(
                this.snFileService.create(this.snFile));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<SnFile>>) {
        result.subscribe((res: HttpResponse<SnFile>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: SnFile) {
        this.eventManager.broadcast({ name: 'snFileListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackSnFileBlobById(index: number, item: SnFileBlob) {
        return item.id;
    }

    trackPatientById(index: number, item: Patient) {
        return item.id;
    }

    trackTaskById(index: number, item: Task) {
        return item.id;
    }

    trackUserInfoById(index: number, item: UserInfo) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-sn-file-popup',
    template: ''
})
export class SnFilePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private snFilePopupService: SnFilePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.snFilePopupService
                    .open(SnFileDialogComponent as Component, params['id']);
            } else {
                this.snFilePopupService
                    .open(SnFileDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
