import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { SnFileComponent } from './sn-file.component';
import { SnFileDetailComponent } from './sn-file-detail.component';
import { SnFilePopupComponent } from './sn-file-dialog.component';
import { SnFileDeletePopupComponent } from './sn-file-delete-dialog.component';

export const snFileRoute: Routes = [
    {
        path: 'sn-file',
        component: SnFileComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SnFiles'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sn-file/:id',
        component: SnFileDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SnFiles'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const snFilePopupRoute: Routes = [
    {
        path: 'sn-file-new',
        component: SnFilePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SnFiles'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sn-file/:id/edit',
        component: SnFilePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SnFiles'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sn-file/:id/delete',
        component: SnFileDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SnFiles'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
