import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager } from 'ng-jhipster';

import { UserInfo } from './user-info.model';
import { UserInfoService } from './user-info.service';

@Component({
    selector: 'jhi-user-info-detail',
    templateUrl: './user-info-detail.component.html'
})
export class UserInfoDetailComponent implements OnInit, OnDestroy {

    userInfo: UserInfo;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private userInfoService: UserInfoService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInUserInfos();
    }

    load(id) {
        this.userInfoService.find(id)
            .subscribe((userInfoResponse: HttpResponse<UserInfo>) => {
                this.userInfo = userInfoResponse.body;
            });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInUserInfos() {
        this.eventSubscriber = this.eventManager.subscribe(
            'userInfoListModification',
            (response) => this.load(this.userInfo.id)
        );
    }
}
