import { Component, OnInit, EventEmitter, Input, Output } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE, Principal } from '../../../shared';

import { Task } from '../../task/task.model';
import { TaskHistory } from '../../task-history/task-history.model';
import { TaskStatus as HistoryStatus} from '../../task-history/task-history.model';
import { ExtTaskService } from '../ext-task.service';
// import { componentRefresh } from '@angular/core/src/render3/instructions';
// import { OnChanges } from '@angular/core/src/metadata/lifecycle_hooks';

@Component({
  selector: 'jhi-ext-task-list-template',
  templateUrl: './ext-task-list-template.component.html',
  styles: []
})
export class ExtTaskListTemplateComponent implements OnInit {

  @Input() source: string;
  @Input() tasks: Task[];
  @Output() onReload = new EventEmitter<object>();

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

  constructor(
        private extTaskService: ExtTaskService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
            console.log('page = ' + this.page);
        });
    this.datePipe = new DatePipe('en');
  }

  ngOnInit() {
    // console.log('in etlt init');
    this.itemsPerPage = ITEMS_PER_PAGE;
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
    // this.loadPage(this.page);
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

    const historyObe: TaskHistory = new TaskHistory();
    historyObe.taskStatus = HistoryStatus.SETTING;
    historyObe.notes = 'updated peckOrder';

    this.tasks.forEach((task) => {
      task.peckOrder = task.peckOrder + 2;
    });

    this.updateTasks(this.tasks, historyObe, 'peckOrder');
  }

  updateTasks(tasks: Task[], historyObe: TaskHistory, fieldNames: String) {

    this.extTaskService.updateTasks(tasks, historyObe, fieldNames).subscribe(
      (data) => {
        this.jhiAlertService.error(data.body.toString());
        this.uponReload(true);
      },
      (err) => this.jhiAlertService.error(err, null, null),
      () => this.jhiAlertService.success('updated tasks', null, null)
    );
  }

  composeUrlParam() {
    const urlParamObj: object = {
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
    const urlParamObj = this.composeUrlParam();
    // console.log('etlt child before Reload taskLen = ');
    this.onReload.emit(urlParamObj);
    // this.onReload(null);
    // console.log('etlt child after Reload');
    // this.progress.isUploaded = false;
  }
}
