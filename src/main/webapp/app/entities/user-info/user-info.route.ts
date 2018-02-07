import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { UserInfoComponent } from './user-info.component';
import { UserInfoDetailComponent } from './user-info-detail.component';
import { UserInfoPopupComponent } from './user-info-dialog.component';
import { UserInfoDeletePopupComponent } from './user-info-delete-dialog.component';

export const userInfoRoute: Routes = [
    {
        path: 'user-info',
        component: UserInfoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserInfos'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'user-info/:id',
        component: UserInfoDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserInfos'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const userInfoPopupRoute: Routes = [
    {
        path: 'user-info-new',
        component: UserInfoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserInfos'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-info/:id/edit',
        component: UserInfoPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserInfos'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'user-info/:id/delete',
        component: UserInfoDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'UserInfos'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
