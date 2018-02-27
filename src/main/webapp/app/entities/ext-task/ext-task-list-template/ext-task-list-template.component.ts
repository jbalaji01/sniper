import { Component, OnInit, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE, Principal } from '../../../shared';

import {SnFile} from '../../sn-file/sn-file.model';

import { Task, TaskStatus } from '../../task/task.model';
import { TaskHistory } from '../../task-history/task-history.model';
import { TaskStatus as HistoryStatus} from '../../task-history/task-history.model';
import { ExtTaskService } from '../ext-task.service';

import { DownloaderComponent } from '../downloader/downloader.component';

// import { componentRefresh } from '@angular/core/src/render3/instructions';
// import { OnChanges } from '@angular/core/src/metadata/lifecycle_hooks';

@Component({
  selector: 'jhi-ext-task-list-template',
  templateUrl: './ext-task-list-template.component.html',
  styles: []
})
export class ExtTaskListTemplateComponent implements OnInit {

  @Input() source: string;
  @Input() taskGroupId: number;
  @Output() onReload = new EventEmitter<object>();

  // @ViewChild('selectedTasksDownloader') selectedTasksDownloader: DownloaderComponent;
    tasks: Task[];
    bundleMap: object[];

    // clear these lists when corresponding pop-up is closed
    snFiles: SnFile[];
    historyList: TaskHistory[];

    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;
    eventSubscriber: Subscription;

    fromDate: string;
    toDate: string;

    datePipe: DatePipe;

    // when task table checkbox is ticked or unticked, update this list
    selectedTasks: Task[];

  constructor(
        private extTaskService: ExtTaskService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
  ) {

  }

  ngOnInit() {
    // console.log('in etlt init');
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 1;
    this.reverse = false;
    this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
    this.datePipe = new DatePipe('en');

    this.today();
    this.previousMonth();
    this.onChangeDate();
    this.registerChangeInTaskTemplate();
    this.loadBundle();
  }

  onChangeDate() {
    this.loadAll();
  }

  previousMonth() {
    const dateFormat = 'yyyy-MM-dd';
    let fromDate: Date = new Date();

    if (fromDate.getMonth() === 0) {
        fromDate = new Date(fromDate.getFullYear() - 1, 11, fromDate.getDate());
    } else {
        fromDate = new Date(fromDate.getFullYear(), fromDate.getMonth() - 1, fromDate.getDate());
    }

    this.fromDate = this.datePipe.transform(fromDate, dateFormat);
  }

  today() {
    const dateFormat = 'yyyy-MM-dd';
    // Today + 1 day - needed if the current day must be included
    // this logic is done in server
    const today: Date = new Date();
    // today.setDate(today.getDate() + 1);
    // const date = new Date(today.getFullYear(), today.getMonth(), today.getDate());
    // this.toDate = this.datePipe.transform(date, dateFormat);
    this.toDate = this.datePipe.transform(today, dateFormat);
  }

  loadAll() {
    this.loadPage(this.page);
  }

  loadPage(page: number) {
    this.page = page;
    this.uponReload(true);
  }

  loadTasks(urlParamObj) {
    // console.log('inside parent loadTasksOfTaskGroup');
    // const obj = urlParamObj['map'];
    // obj['taskGroupId'] = this.taskGroupId;
    this.extTaskService.queryTasks(urlParamObj).subscribe(
        (data) => {
          this.tasks = data.body;
          // this.templateComponent.setParams(this.tasks);
        },
        (err) => this.jhiAlertService.error(err.detail, null, null),
        () => this.jhiAlertService.success('loaded tasks', null, null)
    );
  }

  loadBundle() {
    this.extTaskService.fetchBundle()
    .subscribe(
      (data) => {
        this.bundleMap = data;
        // console.log('teest 123  ' + JSON.stringify(this.bundleMap));
        // console.log('test aaa ' +  BUNDLE_FIELD.HOSPITAL + ' ' + BUNDLE_FIELD[BUNDLE_FIELD.HOSPITAL]);
        // console.log('hospi ' + JSON.stringify(this.bundleMap[BUNDLE_FIELD.HOSPITAL][0]));
        // console.log('doci ' + JSON.stringify(this.bundleMap[BUNDLE_FIELD.DOCTOR][0]));
      },
      (err) => this.jhiAlertService.error(err.detail, null, null),
      () => this.jhiAlertService.success('loaded files info', null, null)
    );
  }

  refresh() {
    const urlParamObj = this.composeUrlParam();
    this.loadTasks(urlParamObj);
  }

   // get the snFiles of the task id
   loadSnFiles(taskId) {
    this.extTaskService.findSnFiles(taskId)
    .subscribe(
      (data) => {
        this.snFiles = data;
      },
      (err) => this.jhiAlertService.error(err.detail, null, null),
      () => this.jhiAlertService.success('loaded files info', null, null)
  );
  }

     // get the history of the task id
  loadHistory(taskId) {
    this.extTaskService.findHistory(taskId).subscribe(
        (data) => {
          this.historyList = data;
        },
        (err) => this.jhiAlertService.error(err.detail, null, null),
        () => this.jhiAlertService.success('loaded history list', null, null)
    );
    }

  trackId(index: number, item: Task) {
    return item.id;
  }

  registerChangeInTaskTemplate() {
    this.eventSubscriber = this.eventManager.subscribe('taskTemplateModification', (response) => this.loadAll());
  }

  sort() {
    // console.log('etgd predicate=' + this.predicate);
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
        result.push('id');
    }
    return result;
  }

  transition() {
    const destinationRoute: string[] = this.source === 'taskGroup' ?
                 ['../ext-task-group-detail', this.tasks[0].taskGroup.id] :
                 null;
    this.router.navigate(destinationRoute, this.composeUrlParam());
    this.loadAll();
  }

  sampleEvent() {
    console.log('in sampleEvent()');
    // this.uponReload(true);
    // console.log(JSON.stringify(this.tasks));

    // const historyObe: TaskHistory = new TaskHistory();
    // historyObe.taskStatus = HistoryStatus.SETTING;
    // historyObe.notes = 'updated peckOrder';

    // this.tasks.forEach((task) => {
    //   task.peckOrder = task.peckOrder + 2;
    // });

    // this.updateTasks(this.tasks, historyObe, 'peckOrder');
    this.updateSelectedTasks();
  }

  // call this fn when task checkbox is ticked or unticked
  updateSelectedTasks() {
    if (this.selectedTasks == null) {
      this.selectedTasks = [];
    }

    this.selectedTasks.length = 0;
    this.selectedTasks.push(this.tasks[0]);
    this.selectedTasks.push(this.tasks[2]);

    // console.log('setting selectedTask with ' + this.selectedTasks.length);
  }

  updateSnFiles(snFiles: SnFile[]) {
    this.extTaskService.updateSnFiles(snFiles).subscribe(
      (data) => {
        this.jhiAlertService.success('success! ' + data.msg);
        console.log('updateSnFiles msg=' + JSON.stringify(data.msg));
      },
      (err) => this.jhiAlertService.error('error in updating snFiles!' + err, null, null),
      () => this.jhiAlertService.success('updated snFiles', null, null)
    );
  }

  updateTasks(tasks: Task[], historyObe: TaskHistory, fieldNames: String) {
    this.extTaskService.updateTasks(tasks, historyObe, fieldNames).subscribe(
      (data) => {
        // this.jhiAlertService.success('success! ' + data.msg);
        console.log('updateTasks msg=' + JSON.stringify(data.msg));
        this.uponReload(true);
      },
      (err) => this.jhiAlertService.error('error in updating tasks!' + err, null, null),
      () => this.jhiAlertService.success('updated tasks', null, null)
    );
  }

  composeUrlParam() {
    const urlParamObj: object = {
      source: this.source,
      taskGroupId: this.taskGroupId,

      fromDate: this.fromDate,
      toDate: this.toDate,
      page: this.page - 1,
      size: this.itemsPerPage,
      sort: this.sort()
    };

    return urlParamObj;
  }

  uponReload(status: boolean) {
    // console.log('etlt child entered uponReload()');
    // const urlParamObj = { 'map': this.composeUrlParam()};
    // console.log('etlt child before Reload taskLen = ');
    const urlParamObj = this.composeUrlParam();
    this.loadTasks(urlParamObj);
    this.onReload.emit(urlParamObj);
    // this.onReload(null);
    // console.log('etlt child after Reload');
    // this.progress.isUploaded = false;
  }

  uponUploadCompletion(taskId: number) {
    this.createHistoryWithId(taskId, TaskStatus.UPLOADED, 'updated taskStatus to upload');
    this.loadAll();
  }

  uponDownloadCompletion(taskId: number) {
    this.createHistoryWithId(taskId, TaskStatus.DOWNLOADED, 'updated taskStatus to download');
  }

  uponSelectedTasksDownloadCompletion(dummyTaskId: number) {

    this.selectedTasks.forEach((task) => {
      task.taskStatus = TaskStatus.DOWNLOADED;
    });

    const historyObe: TaskHistory = this.composeHistoryObe(TaskStatus.DOWNLOADED, 'updated taskStatus to download');

    this.updateTasks(this.selectedTasks, historyObe, 'taskStatus');
  }

  // scan thru tasks and locate taskId
  findTask(taskId: number): Task {
    return this.tasks.find((x) => x.id === taskId);
  }

  // create history of a task with task id
  createHistoryWithId(taskId: number, taskStatus: TaskStatus, notes: string) {
    const task: Task = this.findTask(taskId);
    if (task == null) {
      this.jhiAlertService.error('unable to locate task with id ' + taskId, null, null);
      return;
    }

    this.createHistoryOfTask(task, taskStatus, notes);
  }

  // create task's history
  createHistoryOfTask(task: Task, taskStatus: TaskStatus, notes: string) {
    task.taskStatus = taskStatus;

    const historyObe: TaskHistory = this.composeHistoryObe(taskStatus, notes);

    this.updateTasks([task], historyObe, 'taskStatus');
  }

  composeHistoryObe(taskStatus: TaskStatus, notes: string): TaskHistory {
    const historyObe: TaskHistory = new TaskHistory();
    historyObe.taskStatus =
        taskStatus === TaskStatus.DOWNLOADED ? HistoryStatus.DOWNLOADED :
        taskStatus === TaskStatus.UPLOADED ? HistoryStatus.UPLOADED :
        HistoryStatus.SETTING;
    historyObe.notes = notes;

    return historyObe;
  }

}

export enum BUNDLE_FIELD {
  USER  = 'USER',
  COMPANY = 'COMPANY',
  HOSPITAL = 'HOSPITAL',
  DOCTOR = 'DOCTOR',
  OWNER = 'OWNER',
  TRANSCRIPT = 'TRANSCRIPT',
  EDITOR = 'EDITOR',
  MANAGER = 'MANAGER'
}
