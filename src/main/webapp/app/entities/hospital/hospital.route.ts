import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { HospitalComponent } from './hospital.component';
import { HospitalDetailComponent } from './hospital-detail.component';
import { HospitalPopupComponent } from './hospital-dialog.component';
import { HospitalDeletePopupComponent } from './hospital-delete-dialog.component';

export const hospitalRoute: Routes = [
    {
        path: 'hospital',
        component: HospitalComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Hospitals'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'hospital/:id',
        component: HospitalDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Hospitals'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const hospitalPopupRoute: Routes = [
    {
        path: 'hospital-new',
        component: HospitalPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Hospitals'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'hospital/:id/edit',
        component: HospitalPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Hospitals'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'hospital/:id/delete',
        component: HospitalDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Hospitals'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
