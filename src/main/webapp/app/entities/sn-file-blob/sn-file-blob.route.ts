import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { SnFileBlobComponent } from './sn-file-blob.component';
import { SnFileBlobDetailComponent } from './sn-file-blob-detail.component';
import { SnFileBlobPopupComponent } from './sn-file-blob-dialog.component';
import { SnFileBlobDeletePopupComponent } from './sn-file-blob-delete-dialog.component';

export const snFileBlobRoute: Routes = [
    {
        path: 'sn-file-blob',
        component: SnFileBlobComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SnFileBlobs'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'sn-file-blob/:id',
        component: SnFileBlobDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SnFileBlobs'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const snFileBlobPopupRoute: Routes = [
    {
        path: 'sn-file-blob-new',
        component: SnFileBlobPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SnFileBlobs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sn-file-blob/:id/edit',
        component: SnFileBlobPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SnFileBlobs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'sn-file-blob/:id/delete',
        component: SnFileBlobDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'SnFileBlobs'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
