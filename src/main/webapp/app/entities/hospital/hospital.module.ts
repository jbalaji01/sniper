import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SniperSharedModule } from '../../shared';
import {
    HospitalService,
    HospitalPopupService,
    HospitalComponent,
    HospitalDetailComponent,
    HospitalDialogComponent,
    HospitalPopupComponent,
    HospitalDeletePopupComponent,
    HospitalDeleteDialogComponent,
    hospitalRoute,
    hospitalPopupRoute,
} from './';

const ENTITY_STATES = [
    ...hospitalRoute,
    ...hospitalPopupRoute,
];

@NgModule({
    imports: [
        SniperSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        HospitalComponent,
        HospitalDetailComponent,
        HospitalDialogComponent,
        HospitalDeleteDialogComponent,
        HospitalPopupComponent,
        HospitalDeletePopupComponent,
    ],
    entryComponents: [
        HospitalComponent,
        HospitalDialogComponent,
        HospitalPopupComponent,
        HospitalDeleteDialogComponent,
        HospitalDeletePopupComponent,
    ],
    providers: [
        HospitalService,
        HospitalPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SniperHospitalModule {}
