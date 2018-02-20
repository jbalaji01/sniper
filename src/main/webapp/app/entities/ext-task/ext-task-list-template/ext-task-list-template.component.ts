import { Component, OnInit, EventEmitter, Input, Output, DoCheck, IterableDiffers,
  ChangeDetectorRef, ApplicationRef, NgZone } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { ITEMS_PER_PAGE, Principal } from '../../../shared';

import { Task } from '../../task/task.model';
import { componentRefresh } from '@angular/core/src/render3/instructions';
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

    differ: IterableDiffers;

  constructor(
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager,
        private differs: IterableDiffers,
        private cdRef: ChangeDetectorRef,
        private appRef: ApplicationRef,
        public zone: NgZone
  ) {
    // this.itemsPerPage = ITEMS_PER_PAGE;
    // this.routeData = this.activatedRoute.data.subscribe((data) => {
    //         this.page = data.pagingParams.page;
    //         this.previousPage = data.pagingParams.page;
    //         this.reverse = data.pagingParams.ascending;
    //         this.predicate = data.pagingParams.predicate;
    //     });
    // this.datePipe = new DatePipe('en');
    // this.differ = this.differs.find([]).create(null);
  }

  ngOnInit() {
    // console.log('in etlt init');
    // this.onChangeDate();
    this.registerChangeInTaskTemplate();
    // this.loadPage(this.page);
  }

  initialize() {
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
  }

  setParams(foriegnTasks: Task[]) {
    console.log('** in child setParams() - ' + foriegnTasks.length);
    this.tasks = JSON.parse(JSON.stringify(foriegnTasks));
    // this.zone.run(() => {});
    // this.tasks.push(new Task(100));
    // this.cdRef.detectChanges();
    // this.appRef.tick();

    // this.tasks.length = 0;
    // this.tasks = [];
    // foriegnTasks.forEach((task) => {
    //   // this.tasks.push(task);
    //   console.log(task);
    // });

    console.log('** in child this.tasks.length - ' + this.tasks.length);
    console.log(JSON.stringify(this.tasks));
  }

  // ngOnChanges(changes) {
  //   console.log(this.source); // new value updated
  // }

  // ngDoCheck() {
  //   // var changes = this.differ.diff(this.posts);
  //   // if (changes) {
  //   //   console.log('ngDoCheck');
  //   //   this.status = 'ngDoCheck invoked!'
  //   // }
  // }

  onChangeDate() {
    this.loadPage(this.page);
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

  sampleEvent() {
    console.log('in sampleEvent()');
    // this.uponReload(true);
    console.log(JSON.stringify(this.tasks));
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
    console.log('etlt child entered uponReload()');
    const urlParamObj = this.composeUrlParam();
    console.log('etlt child before Reload taskLen = ');
    this.onReload.emit(urlParamObj);
    // this.onReload(null);
    console.log('etlt child after Reload');
    // this.progress.isUploaded = false;
  }
}
