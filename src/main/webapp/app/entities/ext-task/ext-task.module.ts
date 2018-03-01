import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

import { NgbActiveModal, NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SniperSharedModule } from '../../shared';

import { ExtTaskRoutingModule } from './ext-task-routing.module';
import { DownloaderComponent } from './downloader/downloader.component';
import { PurgeComponent } from './purge/purge.component';
import { ExtTaskGroupDetailComponent } from './ext-task-group-detail/ext-task-group-detail.component';
import { ExtTaskGroupListComponent } from './ext-task-group-list/ext-task-group-list.component';
import { ExtTaskListComponent } from './ext-task-list/ext-task-list.component';
import { UploaderComponent } from './uploader/uploader.component';
import { HelpComponent } from './help/help.component';

import { ExtTaskService } from './ext-task.service';
import { ExtTaskResolvePagingParams } from './';
import { ExtTaskListTemplateComponent } from './ext-task-list-template/ext-task-list-template.component';
import { ExtSnFileComponent } from './ext-sn-file/ext-sn-file.component';
import { ExtHistoryComponent } from './ext-history/ext-history.component';

import { routes } from './ext-task-routing.module';

// import { JhiItemCountComponent } from '../../shared';

const ENTITY_STATES = [
  ...routes
];

@NgModule({
  imports: [
    RouterModule.forChild(ENTITY_STATES),
    SniperSharedModule,
    CommonModule,
    ExtTaskRoutingModule
  ],
  declarations: [DownloaderComponent, PurgeComponent, ExtTaskGroupDetailComponent, ExtTaskGroupListComponent, ExtTaskListComponent,
                 UploaderComponent, HelpComponent, ExtTaskListTemplateComponent, ExtSnFileComponent, ExtHistoryComponent],
  providers: [ExtTaskService, ExtTaskResolvePagingParams, NgbActiveModal],
  entryComponents: [ExtSnFileComponent, ExtHistoryComponent],
})
export class ExtTaskModule { }
