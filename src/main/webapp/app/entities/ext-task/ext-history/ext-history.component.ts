import { Component, OnInit, OnDestroy } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs/Subscription';
import { ActivatedRoute } from '@angular/router';
import { JhiAlertService } from 'ng-jhipster';

import { TaskHistory } from '../../task-history/task-history.model';
import { ExtTaskService } from '../ext-task.service';

@Component({
  selector: 'jhi-ext-history',
  templateUrl: './ext-history.component.html',
  styles: []
})
export class ExtHistoryComponent implements OnInit, OnDestroy {

  private subscription: Subscription;

  taskId: number;
  taskHistories: TaskHistory[];

  constructor(
    public activeModal: NgbActiveModal,
    private extTaskService: ExtTaskService,
    private jhiAlertService: JhiAlertService,
    private activatedRoute: ActivatedRoute
    ) { }

  ngOnInit() {
    this.subscription = this.activatedRoute.params.subscribe((params) => {
      this.taskId = params['id'];
      this.loadAll();
    });
  }

  loadAll() {
    this.loadHistory(this.taskId);
  }

     // get the history of the task id
  loadHistory(taskId) {
    this.extTaskService.findHistory(taskId).subscribe(
        (data) => {
          this.taskHistories = data;
        },
        (err) => this.jhiAlertService.error(err.detail, null, null),
        () => this.jhiAlertService.success('loaded history list', null, null)
     );
  }

  previousState() {
    window.history.back();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  trackId(index: number, item: TaskHistory) {
    return item.id;
  }

}
