import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { TaskGroup } from '../../task-group/task-group.model';
import { TaskGroupService } from '../../task-group/task-group.service';
import { ITEMS_PER_PAGE, Principal } from '../../../shared';
import { ExtTaskService } from '../ext-task.service';

@Component({
  selector: 'jhi-ext-task-group-list',
  templateUrl: './ext-task-group-list.component.html',
  styles: []
})
export class ExtTaskGroupListComponent implements OnInit, OnDestroy {

    currentAccount: any;
    taskGroups: TaskGroup[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    links: any;
    totalItems: any;
    queryCount: any;
    itemsPerPage: any;
    page: any;
    predicate: any;
    previousPage: any;
    reverse: any;

    fromDate: string;
    toDate: string;

    datePipe: DatePipe;

    constructor(
        private taskGroupService: TaskGroupService,
        private extTaskService: ExtTaskService,
        private parseLinks: JhiParseLinks,
        private jhiAlertService: JhiAlertService,
        private principal: Principal,
        private activatedRoute: ActivatedRoute,
        private router: Router,
        private eventManager: JhiEventManager
    ) {
        this.itemsPerPage = ITEMS_PER_PAGE;
        this.page = 1;
        this.reverse = false;
        // this.orderProp = 'timestamp';
        this.routeData = this.activatedRoute.data.subscribe((data) => {
            this.page = data.pagingParams.page;
            this.previousPage = data.pagingParams.page;
            this.reverse = data.pagingParams.ascending;
            this.predicate = data.pagingParams.predicate;
        });
        this.datePipe = new DatePipe('en');
    }

  loadAll() {
    //   console.log('etgl loadAll');
        this.extTaskService.queryTaskGroupsByDate({
            page: this.page - 1,
            size: this.itemsPerPage,
            fromDate: this.fromDate,
            toDate: this.toDate,
            sort: this.sort()}).subscribe(
                (data) => { this.taskGroups = data.body; },
                (err) => this.jhiAlertService.error(err, null, null),
                () => this.jhiAlertService.success('loaded taskGroups', null, null)
        );

        /* this.extTaskService.queryTaskGroupsByDate({
            page: this.page - 1,
            size: this.itemsPerPage,
            fromDate: this.fromDate,
            toDate: this.toDate,
            sort: this.sort()}).subscribe(
                (res: HttpResponse<TaskGroup[]>) => this.onSuccess(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
        ); */
    }
    loadPage(page: number) {
        if (page !== this.previousPage) {
            this.previousPage = page;
            this.transition();
        }
    }
    transition() {
        this.router.navigate(['/ext-task-group-list'], {queryParams:
            {
                page: this.page,
                size: this.itemsPerPage,
                sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
            }
        });
        this.loadAll();
    }

    clear() {
        this.page = 0;
        this.router.navigate(['/ext-task-group-list', {
            page: this.page,
            sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
        }]);
        this.loadAll();
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

    trackId(index: number, item: TaskGroup) {
        return item.id;
    }
    registerChangeInTaskGroups() {
        this.eventSubscriber = this.eventManager.subscribe('taskGroupListModification', (response) => this.loadAll());
    }

    ngOnInit() {
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        // console.log('inside init');
        this.registerChangeInTaskGroups();
        // console.log('finished reg');
        this.today();
        // console.log('after today');
        this.previousMonth();
        this.onChangeDate();
    }

    ngOnDestroy() {
      this.eventManager.destroy(this.eventSubscriber);
    }

    sort() {
        const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
        if (this.predicate !== 'id') {
            result.push('id');
        }
        return result;
    }

    private onSuccess(data, headers) {
        this.links = this.parseLinks.parse(headers.get('link'));
        this.totalItems = headers.get('X-Total-Count');
        this.queryCount = this.totalItems;
        // this.page = pagingParams.page;
        this.taskGroups = data;
        console.log('inside etgl onSuccess');
        this.jhiAlertService.success('loaded taskGroups');
    }
    private onError(error) {
        this.jhiAlertService.error(error.message, null, null);
    }

    uponUploadCompletion(id: number) {
        this.jhiAlertService.success('Successfully uploaded files', null, null);
        this.loadAll();
    }
}
