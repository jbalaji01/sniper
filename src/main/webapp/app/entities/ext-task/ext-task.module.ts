import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { SniperSharedModule } from '../../shared';

import { ExtTaskRoutingModule } from './ext-task-routing.module';
import { DownloaderComponent } from './downloader/downloader.component';
import { PurgeComponent } from './purge/purge.component';
import { ExtTaskGroupDetailComponent } from './ext-task-group-detail/ext-task-group-detail.component';
import { ExtTaskGroupListComponent } from './ext-task-group-list/ext-task-group-list.component';
import { ExtTaskListComponent } from './ext-task-list/ext-task-list.component';
import { UploaderComponent } from './uploader/uploader.component';
import { HelpComponent } from './help/help.component';

import {ExtTaskService} from './ext-task.service';
import {ExtTaskResolvePagingParams} from './';
import { ExtTaskListTemplateComponent } from './ext-task-list-template/ext-task-list-template.component';

// import { JhiItemCountComponent } from '../../shared';

@NgModule({
  imports: [
    SniperSharedModule,
    CommonModule,
    ExtTaskRoutingModule
  ],
  declarations: [DownloaderComponent, PurgeComponent, ExtTaskGroupDetailComponent, ExtTaskGroupListComponent, ExtTaskListComponent,
                 UploaderComponent, HelpComponent, ExtTaskListTemplateComponent],
  providers: [ExtTaskService, ExtTaskResolvePagingParams],
})
export class ExtTaskModule { }
