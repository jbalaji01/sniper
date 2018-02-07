import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SniperSharedModule } from '../../shared';
import {
    SnFileBlobService,
    SnFileBlobPopupService,
    SnFileBlobComponent,
    SnFileBlobDetailComponent,
    SnFileBlobDialogComponent,
    SnFileBlobPopupComponent,
    SnFileBlobDeletePopupComponent,
    SnFileBlobDeleteDialogComponent,
    snFileBlobRoute,
    snFileBlobPopupRoute,
} from './';

const ENTITY_STATES = [
    ...snFileBlobRoute,
    ...snFileBlobPopupRoute,
];

@NgModule({
    imports: [
        SniperSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SnFileBlobComponent,
        SnFileBlobDetailComponent,
        SnFileBlobDialogComponent,
        SnFileBlobDeleteDialogComponent,
        SnFileBlobPopupComponent,
        SnFileBlobDeletePopupComponent,
    ],
    entryComponents: [
        SnFileBlobComponent,
        SnFileBlobDialogComponent,
        SnFileBlobPopupComponent,
        SnFileBlobDeleteDialogComponent,
        SnFileBlobDeletePopupComponent,
    ],
    providers: [
        SnFileBlobService,
        SnFileBlobPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SniperSnFileBlobModule {}
