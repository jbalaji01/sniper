import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'jhi-ext-task-list',
  templateUrl: './ext-task-list.component.html',
  styles: ['./ext-task-list.component.scss']
})
export class ExtTaskListComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

  public handleClick(event: Event) {
    event.preventDefault();
  }
}
