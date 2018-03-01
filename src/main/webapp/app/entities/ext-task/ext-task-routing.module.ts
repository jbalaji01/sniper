import { NgModule, Injectable } from '@angular/core';
import { Resolve, Routes, RouterModule, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { JhiPaginationUtil } from 'ng-jhipster';
import { UserRouteAccessService } from '../../shared';

import { DownloaderComponent } from './downloader/downloader.component';
import { PurgeComponent } from './purge/purge.component';
import { ExtTaskGroupDetailComponent } from './ext-task-group-detail/ext-task-group-detail.component';
import { ExtTaskGroupListComponent } from './ext-task-group-list/ext-task-group-list.component';
import { ExtTaskListComponent } from './ext-task-list/ext-task-list.component';
import { UploaderComponent } from './uploader/uploader.component';
import { HelpComponent } from './help/help.component';
import { ExtSnFileComponent, ExtSnFilePopupComponent } from './ext-sn-file/ext-sn-file.component';
import { ExtHistoryComponent } from './ext-history/ext-history.component';

@Injectable()
export class ExtTaskResolvePagingParams implements Resolve<any> {

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

export const routes: Routes = [
    {
        path: 'downloader',
        component: DownloaderComponent,
        resolve: {
            'pagingParams': ExtTaskResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sniper - Download files'
        },
        canActivate: [UserRouteAccessService]
    },

    {
        path: 'uploader',
        component: UploaderComponent,
        resolve: {
            'pagingParams': ExtTaskResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sniper - Upload files'
        },
        canActivate: [UserRouteAccessService]
    },

    {
        path: 'ext-task-group-detail/:id',
        component: ExtTaskGroupDetailComponent,
        resolve: {
            'pagingParams': ExtTaskResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sniper - task group detail'
        },
        canActivate: [UserRouteAccessService]
    },

    {
        path: 'ext-task-group-list',
        component: ExtTaskGroupListComponent,
        resolve: {
            'pagingParams': ExtTaskResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sniper - task group list'
        },
        canActivate: [UserRouteAccessService]
    },

    {
        path: 'ext-task-list',
        component: ExtTaskListComponent,
        resolve: {
            'pagingParams': ExtTaskResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sniper - Task list'
        },
        canActivate: [UserRouteAccessService]
    },

    {
        path: 'help',
        component: HelpComponent,
        resolve: {
            'pagingParams': ExtTaskResolvePagingParams
        },
        data: {
            authorities: [],
            pageTitle: 'Sniper - Help'
        },
        canActivate: [UserRouteAccessService]
    },

    {
        path: 'purge',
        component: PurgeComponent,
        resolve: {
            'pagingParams': ExtTaskResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sniper - Purge'
        },
        canActivate: [UserRouteAccessService]
    },

    {
        path: 'ext-sn-file/:id',
        component: ExtSnFileComponent,
        //   ExtSnFilePopupComponent,
        resolve: {
            'pagingParams': ExtTaskResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sniper - files of task'
        },
        canActivate: [UserRouteAccessService],
        // outlet: 'popup'
    },

    {
        path: 'ext-history/:id',
        component: ExtHistoryComponent,
        resolve: {
            'pagingParams': ExtTaskResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Sniper - history of task'
        },
        canActivate: [UserRouteAccessService],
        // outlet: 'popup'
    },

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class ExtTaskRoutingModule { }
