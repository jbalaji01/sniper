import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { UserInfo } from './user-info.model';
import { UserInfoPopupService } from './user-info-popup.service';
import { UserInfoService } from './user-info.service';

@Component({
    selector: 'jhi-user-info-delete-dialog',
    templateUrl: './user-info-delete-dialog.component.html'
})
export class UserInfoDeleteDialogComponent {

    userInfo: UserInfo;

    constructor(
        private userInfoService: UserInfoService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.userInfoService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'userInfoListModification',
                content: 'Deleted an userInfo'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-user-info-delete-popup',
    template: ''
})
export class UserInfoDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private userInfoPopupService: UserInfoPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.userInfoPopupService
                .open(UserInfoDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
