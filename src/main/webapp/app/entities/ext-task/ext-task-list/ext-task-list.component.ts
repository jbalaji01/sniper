import { Component, OnInit, ViewChild } from '@angular/core';

import { ExtTaskListTemplateComponent } from '../ext-task-list-template/ext-task-list-template.component';

@Component({
  selector: 'jhi-ext-task-list',
  templateUrl: './ext-task-list.component.html',
  styles: ['./ext-task-list.component.scss']
})
export class ExtTaskListComponent implements OnInit {

  @ViewChild('activeTasksTemplate') activeTasksTemplate: ExtTaskListTemplateComponent;
  @ViewChild('allTasksTemplate') allTasksTemplate: ExtTaskListTemplateComponent;

  constructor() { }

  ngOnInit() {
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
