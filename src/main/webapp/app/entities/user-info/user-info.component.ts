import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { UserInfo } from './user-info.model';
import { UserInfoService } from './user-info.service';
import { Principal } from '../../shared';

@Component({
    selector: 'jhi-user-info',
    templateUrl: './user-info.component.html'
})
export class UserInfoComponent implements OnInit, OnDestroy {
userInfos: UserInfo[];
    currentAccount: any;
    eventSubscriber: Subscription;

    constructor(
        private userInfoService: UserInfoService,
        private jhiAlertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private principal: Principal
    ) {
    }

    loadAll() {
        this.userInfoService.query().subscribe(
            (res: HttpResponse<UserInfo[]>) => {
                this.userInfos = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInUserInfos();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: UserInfo) {
        return item.id;
    }
    registerChangeInUserInfos() {
        this.eventSubscriber = this.eventManager.subscribe('userInfoListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }
}
