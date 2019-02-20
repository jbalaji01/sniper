import { Component, OnInit, OnDestroy, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Subscription';

import { ExtTaskListTemplateComponent } from '../ext-task-list-template/ext-task-list-template.component';

@Component({
  selector: 'jhi-ext-task-list',
  templateUrl: './ext-task-list.component.html',
  styles: ['./ext-task-list.component.scss']
})
export class ExtTaskListComponent implements OnInit, OnDestroy {

  private subscription: Subscription;

  key: string;
  isActive = true;

  @ViewChild('activeTasksTemplate') activeTasksTemplate: ExtTaskListTemplateComponent;
  @ViewChild('allTasksTemplate') allTasksTemplate: ExtTaskListTemplateComponent;

  constructor(private activatedRoute: ActivatedRoute) { }

  ngOnInit() {
    this.subscription = this.activatedRoute.params.subscribe((params) => {
      this.key = params['key'];
      this.isActive = this.key === 'active';
    });
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  public handleClick(event: Event) {
    event.preventDefault();
  }

  reload(urlParamObj: any) {
    console.log('inside task list ' + JSON.stringify(urlParamObj));
    // if (urlParamObj && urlParamObj['source'] === 'allTasks') {
    //   this.activeTasksTemplate.refresh();
    // } else {
    //   this.allTasksTemplate.refresh();
    // }
  }
}
