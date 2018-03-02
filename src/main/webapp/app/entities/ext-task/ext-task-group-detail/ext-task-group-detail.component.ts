import { Component, OnInit, OnDestroy } from '@angular/core';

import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { Subscription } from 'rxjs/Subscription';

import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE, Principal } from '../../../shared';

import { Task } from '../../task/task.model';
import { TaskGroup } from '../../task-group/task-group.model';
import { TaskGroupService } from '../../task-group/task-group.service';
import { ExtTaskService } from '../ext-task.service';
// import { ExtTaskListTemplateComponent } from '../ext-task-list-template/ext-task-list-template.component';

@Component({
  selector: 'jhi-ext-task-group-detail',
  templateUrl: './ext-task-group-detail.component.html',
  styles: []
})
export class ExtTaskGroupDetailComponent implements OnInit, OnDestroy {

  private subscription: Subscription;
  private eventSubscriber: Subscription;

  /*
  totalItems: any;
  queryCount: any;
  itemsPerPage: any;
  page: any;
  predicate: any;
  previousPage: any;
  reverse: any;
  */

  routeData: any;

  taskGroupId: number;
  taskGroup: TaskGroup;
  // tasks: Task[];

  statusCountList: [any];
  totalTasks: number;

  constructor(
      private eventManager: JhiEventManager,
      private taskGroupService: TaskGroupService,
      private extTaskService: ExtTaskService,
      private activatedRoute: ActivatedRoute,
      private jhiAlertService: JhiAlertService
  ) {

  }

  ngOnInit() {
    this.subscription = this.activatedRoute.params.subscribe((params) => {
      this.taskGroupId = params['id'];
      this.loadAll();
    });
    this.registerChangeInTaskGroups();
  }

  loadAll() {
    // const urlParamObj = this.templateComponent.composeUrlParam();
    // this.loadWithUrlParamObj(urlParamObj);
    this.loadTaskGroup(this.taskGroupId);
    this.prepareChart(this.taskGroupId);
  }

  // loadWithUrlParamObj(urlParamObj) {
  //   // console.log('etgd urlParam=' + JSON.stringify(urlParamObj));
  //   this.loadTaskGroup(this.taskGroupId);
  //   // this.loadTasksOfTaskGroup(urlParamObj);
  // }

  // get the taskGroup of this id
  loadTaskGroup(id) {
      this.taskGroupService.find(id)
          .subscribe((taskGroupResponse: HttpResponse<TaskGroup>) => {
              this.taskGroup = taskGroupResponse.body;
          });
  }

  // loadTasksOfTaskGroup(urlParamObj) {
  //   console.log('inside parent loadTasksOfTaskGroup');
  //   // const obj = urlParamObj['map'];
  //   // obj['taskGroupId'] = this.taskGroupId;
  //   urlParamObj['taskGroupId'] = this.taskGroupId;
  //   this.extTaskService.queryTasksOfTaskGroup(urlParamObj).subscribe(
  //       (data) => {
  //         this.tasks = data.body;
  //         // this.templateComponent.setParams(this.tasks);
  //       },
  //       (err) => this.jhiAlertService.error(err.detail, null, null),
  //       () => this.jhiAlertService.success('loaded taskGroup and tasks', null, null)
  //   );
  // }

  // sort() {
  //   // console.log('etgd predicate=' + this.predicate);
  //   const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
  //   if (this.predicate !== 'id') {
  //       result.push('id');
  //   }
  //   return result;
  // }

  prepareChart(taskGroupId: number) {
    this.extTaskService.countTaskStatus(taskGroupId)
    .subscribe((response: [any]) => {
        // const data = response.body;
        console.log(JSON.stringify(response));
        this.statusCountList = response;

        this.totalTasks = 0;
        this.statusCountList.forEach((statusCount) => this.totalTasks += statusCount.ord);
    });
  }

  previousState() {
      window.history.back();
  }

  ngOnDestroy() {
      this.subscription.unsubscribe();
      this.eventManager.destroy(this.eventSubscriber);
  }

  registerChangeInTaskGroups() {
      this.eventSubscriber = this.eventManager.subscribe(
          'extTaskGroupDetailModification',
          (response) => this.loadAll()
      );
  }

  reload(urlParamObj: any) {
    // this.loadWithUrlParamObj(urlParamObj);
    this.loadAll();
    // console.log('parent reload ');
  }

  uponUploadCompletion(status: boolean) {
      this.loadAll();
  }

  uponDownloadCompletion(id: number) {
  }
}
