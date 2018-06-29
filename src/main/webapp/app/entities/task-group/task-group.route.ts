import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';

import { UserRouteAccessService } from '../../shared';
import { TaskGroupComponent } from './task-group.component';
import { TaskGroupDetailComponent } from './task-group-detail.component';
import { TaskGroupPopupComponent } from './task-group-dialog.component';
import { TaskGroupDeletePopupComponent } from './task-group-delete-dialog.component';

@Injectable()
export class TaskGroupResolvePagingParams implements Resolve<any> {

    constructor(private paginationUtil: JhiPaginationUtil) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
        const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
        return {
            page: this.paginationUtil.parsePage(page),
            predicate: this.paginationUtil.parsePredicate(sort),
            ascending: this.paginationUtil.parseAscending(sort)
      };
    }
}

export const taskGroupRoute: Routes = [
    {
        path: 'task-group',
        component: TaskGroupComponent,
        resolve: {
            'pagingParams': TaskGroupResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TaskGroups'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'task-group/:id',
        component: TaskGroupDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TaskGroups'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const taskGroupPopupRoute: Routes = [
    {
        path: 'task-group-new',
        component: TaskGroupPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TaskGroups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'task-group/:id/edit',
        component: TaskGroupPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TaskGroups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'task-group/:id/delete',
        component: TaskGroupDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'TaskGroups'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
