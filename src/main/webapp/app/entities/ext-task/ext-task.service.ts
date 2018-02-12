import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiDateUtils } from 'ng-jhipster';

import { TaskGroup } from '../task-group/task-group.model';
import { createRequestOption } from '../../shared';
import {TaskGroupService} from '../task-group/task-group.service';

@Injectable()
export class ExtTaskService {

  private resourceUrl =  SERVER_API_URL + 'ext/';

  constructor(private http: HttpClient,
              private dateUtils: JhiDateUtils,
              private taskGroupService: TaskGroupService) { }

  query(req?: any): Observable<HttpResponse<TaskGroup[]>> {
    const options = createRequestOption(req);
    return this.http.get<TaskGroup[]>(this.resourceUrl + 'task-groups', { params: options, observe: 'response' })
        .map((res: HttpResponse<TaskGroup[]>) => this.taskGroupService.convertArrayResponse(res));
  }
}
