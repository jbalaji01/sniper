import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { TaskHistoryComponent } from './task-history.component';
import { TaskHistoryDetailComponent } from './task-history-detail.component';
import { TaskHistoryPopupComponent } from './task-history-dialog.component';
import { TaskHistoryDeletePopupComponent } from './task-history-delete-dialog.component';

export const taskHistoryRoute: Routes = [
    {
        path: 'task-history',
        component: TaskHistoryComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TaskHistories'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'task-history/:id',
        component: TaskHistoryDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TaskHistories'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const taskHistoryPopupRoute: Routes = [
    {
        path: 'task-history-new',
        component: TaskHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TaskHistories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'task-history/:id/edit',
        component: TaskHistoryPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TaskHistories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'task-history/:id/delete',
        component: TaskHistoryDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TaskHistories'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
