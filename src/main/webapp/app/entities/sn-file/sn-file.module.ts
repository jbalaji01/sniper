import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SniperSharedModule } from '../../shared';
import {
    SnFileService,
    SnFilePopupService,
    SnFileComponent,
    SnFileDetailComponent,
    SnFileDialogComponent,
    SnFilePopupComponent,
    SnFileDeletePopupComponent,
    SnFileDeleteDialogComponent,
    snFileRoute,
    snFilePopupRoute,
} from './';

const ENTITY_STATES = [
    ...snFileRoute,
    ...snFilePopupRoute,
];

@NgModule({
    imports: [
        SniperSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        SnFileComponent,
        SnFileDetailComponent,
        SnFileDialogComponent,
        SnFileDeleteDialogComponent,
        SnFilePopupComponent,
        SnFileDeletePopupComponent,
    ],
    entryComponents: [
        SnFileComponent,
        SnFileDialogComponent,
        SnFilePopupComponent,
        SnFileDeleteDialogComponent,
        SnFileDeletePopupComponent,
    ],
    providers: [
        SnFileService,
        SnFilePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SniperSnFileModule {}
