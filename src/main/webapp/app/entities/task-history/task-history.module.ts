import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SniperSharedModule } from '../../shared';
import {
    TaskHistoryService,
    TaskHistoryPopupService,
    TaskHistoryComponent,
    TaskHistoryDetailComponent,
    TaskHistoryDialogComponent,
    TaskHistoryPopupComponent,
    TaskHistoryDeletePopupComponent,
    TaskHistoryDeleteDialogComponent,
    taskHistoryRoute,
    taskHistoryPopupRoute,
} from './';

const ENTITY_STATES = [
    ...taskHistoryRoute,
    ...taskHistoryPopupRoute,
];

@NgModule({
    imports: [
        SniperSharedModule,
        RouterModule.forChild(ENTITY_STATES)
    ],
    declarations: [
        TaskHistoryComponent,
        TaskHistoryDetailComponent,
        TaskHistoryDialogComponent,
        TaskHistoryDeleteDialogComponent,
        TaskHistoryPopupComponent,
        TaskHistoryDeletePopupComponent,
    ],
    entryComponents: [
        TaskHistoryComponent,
        TaskHistoryDialogComponent,
        TaskHistoryPopupComponent,
        TaskHistoryDeleteDialogComponent,
        TaskHistoryDeletePopupComponent,
    ],
    providers: [
        TaskHistoryService,
        TaskHistoryPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SniperTaskHistoryModule {}
