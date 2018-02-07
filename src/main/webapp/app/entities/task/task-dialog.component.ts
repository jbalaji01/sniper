import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Task } from './task.model';
import { TaskPopupService } from './task-popup.service';
import { TaskService } from './task.service';
import { Company, CompanyService } from '../company';
import { TaskGroup, TaskGroupService } from '../task-group';
import { UserInfo, UserInfoService } from '../user-info';
import { Doctor, DoctorService } from '../doctor';
import { Hospital, HospitalService } from '../hospital';
import { SnFile, SnFileService } from '../sn-file';

@Component({
    selector: 'jhi-task-dialog',
    templateUrl: './task-dialog.component.html'
})
export class TaskDialogComponent implements OnInit {

    task: Task;
    isSaving: boolean;

    companies: Company[];

    taskgroups: TaskGroup[];

    userinfos: UserInfo[];

    doctors: Doctor[];

    hospitals: Hospital[];

    snfiles: SnFile[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private taskService: TaskService,
        private companyService: CompanyService,
        private taskGroupService: TaskGroupService,
        private userInfoService: UserInfoService,
        private doctorService: DoctorService,
        private hospitalService: HospitalService,
        private snFileService: SnFileService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.companyService.query()
            .subscribe((res: HttpResponse<Company[]>) => { this.companies = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.taskGroupService.query()
            .subscribe((res: HttpResponse<TaskGroup[]>) => { this.taskgroups = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.userInfoService.query()
            .subscribe((res: HttpResponse<UserInfo[]>) => { this.userinfos = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.doctorService.query()
            .subscribe((res: HttpResponse<Doctor[]>) => { this.doctors = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.hospitalService.query()
            .subscribe((res: HttpResponse<Hospital[]>) => { this.hospitals = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.snFileService.query()
            .subscribe((res: HttpResponse<SnFile[]>) => { this.snfiles = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.task.id !== undefined) {
            this.subscribeToSaveResponse(
                this.taskService.update(this.task));
        } else {
            this.subscribeToSaveResponse(
                this.taskService.create(this.task));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<Task>>) {
        result.subscribe((res: HttpResponse<Task>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: Task) {
        this.eventManager.broadcast({ name: 'taskListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackCompanyById(index: number, item: Company) {
        return item.id;
    }

    trackTaskGroupById(index: number, item: TaskGroup) {
        return item.id;
    }

    trackUserInfoById(index: number, item: UserInfo) {
        return item.id;
    }

    trackDoctorById(index: number, item: Doctor) {
        return item.id;
    }

    trackHospitalById(index: number, item: Hospital) {
        return item.id;
    }

    trackSnFileById(index: number, item: SnFile) {
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
    selector: 'jhi-task-popup',
    template: ''
})
export class TaskPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private taskPopupService: TaskPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.taskPopupService
                    .open(TaskDialogComponent as Component, params['id']);
            } else {
                this.taskPopupService
                    .open(TaskDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
