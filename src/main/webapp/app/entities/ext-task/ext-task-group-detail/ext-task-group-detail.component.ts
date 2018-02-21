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
  // datePipe: DatePipe;

  taskGroupId: number;
  taskGroup: TaskGroup;
  tasks: Task[];
  // fromDate: string;
  // toDate: string;

  constructor(
      private eventManager: JhiEventManager,
      private taskGroupService: TaskGroupService,
      private extTaskService: ExtTaskService,
      private activatedRoute: ActivatedRoute,
      private jhiAlertService: JhiAlertService
  ) {
    // this.itemsPerPage = ITEMS_PER_PAGE;
    // this.page = 1;
    // this.reverse = false;
    // // this.orderProp = 'timestamp';
    // this.routeData = this.activatedRoute.data.subscribe((data) => {
    //     this.page = data.pagingParams.page;
    //     this.previousPage = data.pagingParams.page;
    //     this.reverse = data.pagingParams.ascending;
    //     this.predicate = data.pagingParams.predicate;
    // });
    // this.datePipe = new DatePipe('en');

  }

  ngOnInit() {
    // this.today();
    // this.previousMonth();
    // console.log('in etgd init');
    this.subscription = this.activatedRoute.params.subscribe((params) => {
      this.taskGroupId = params['id'];
      // console.log('etgd id=' + this.taskGroupId);
      // this.templateComponent.initialize();
      // this.loadAll();
    });
    this.registerChangeInTaskGroups();
  }

  // previousMonth() {
  //   const dateFormat = 'yyyy-MM-dd';
  //   let fromDate: Date = new Date();

  //   if (fromDate.getMonth() === 0) {
  //       fromDate = new Date(fromDate.getFullYear() - 1, 11, fromDate.getDate());
  //   } else {
  //       fromDate = new Date(fromDate.getFullYear(), fromDate.getMonth() - 1, fromDate.getDate());
  //   }

  //   // this.fromDate = this.datePipe.transform(fromDate, dateFormat);
  // }

// today() {
//     const dateFormat = 'yyyy-MM-dd';
//     // Today + 1 day - needed if the current day must be included
//     // this logic is done in server
//     const today: Date = new Date();
//     // today.setDate(today.getDate() + 1);
//     // const date = new Date(today.getFullYear(), today.getMonth(), today.getDate());
//     // this.toDate = this.datePipe.transform(date, dateFormat);
//     this.toDate = this.datePipe.transform(today, dateFormat);
// }

  loadAll() {
    // const urlParamObj = this.templateComponent.composeUrlParam();
    // this.loadWithUrlParamObj(urlParamObj);
  }

  loadWithUrlParamObj(urlParamObj) {
    // console.log('etgd urlParam=' + JSON.stringify(urlParamObj));
    this.loadTaskGroup(this.taskGroupId);
    this.loadTasksOfTaskGroup(urlParamObj);
  }

  // get the taskGroup of this id
  loadTaskGroup(id) {
      this.taskGroupService.find(id)
          .subscribe((taskGroupResponse: HttpResponse<TaskGroup>) => {
              this.taskGroup = taskGroupResponse.body;
          });
  }

  loadTasksOfTaskGroup(urlParamObj) {
    console.log('inside parent loadTasksOfTaskGroup');
    urlParamObj['taskGroupId'] = this.taskGroupId;
    this.extTaskService.queryTasksOfTaskGroup(urlParamObj).subscribe(
        (data) => {
          this.tasks = data.body;
          // this.templateComponent.setParams(this.tasks);
        },
        (err) => this.jhiAlertService.error(err, null, null),
        () => this.jhiAlertService.success('loaded taskGroup and tasks', null, null)
    );
  }

  // sort() {
  //   // console.log('etgd predicate=' + this.predicate);
  //   const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
  //   if (this.predicate !== 'id') {
  //       result.push('id');
  //   }
  //   return result;
  // }

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
    this.loadWithUrlParamObj(urlParamObj);
    console.log('parent reload ');
  }
}
