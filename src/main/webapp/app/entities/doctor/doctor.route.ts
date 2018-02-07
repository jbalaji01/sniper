import { Routes } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { DoctorComponent } from './doctor.component';
import { DoctorDetailComponent } from './doctor-detail.component';
import { DoctorPopupComponent } from './doctor-dialog.component';
import { DoctorDeletePopupComponent } from './doctor-delete-dialog.component';

export const doctorRoute: Routes = [
    {
        path: 'doctor',
        component: DoctorComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Doctors'
        },
        canActivate: [UserRouteAccessService]
    }, {
        path: 'doctor/:id',
        component: DoctorDetailComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Doctors'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const doctorPopupRoute: Routes = [
    {
        path: 'doctor-new',
        component: DoctorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Doctors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'doctor/:id/edit',
        component: DoctorPopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Doctors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    },
    {
        path: 'doctor/:id/delete',
        component: DoctorDeletePopupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'Doctors'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
