import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpRequest, HttpEvent } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';
import { JhiDateUtils } from 'ng-jhipster';
import { createRequestOption } from '../../shared';

import {SnFile} from '../sn-file/sn-file.model';

import { TaskGroup } from '../task-group/task-group.model';
import { Task } from '../task/task.model';
import { TaskHistory } from '../task-history/task-history.model';
import {TaskGroupService} from '../task-group/task-group.service';
import {TaskService} from '../task/task.service';

@Injectable()
export class ExtTaskService {

  private resourceUrl =  SERVER_API_URL + 'api/ext/';

  constructor(private http: HttpClient,
              private dateUtils: JhiDateUtils,
              private taskGroupService: TaskGroupService,
              private taskService: TaskService,
              private jhiAlertService: JhiAlertService) { }

  /* queryTaskGroupsByDate(req?: any): Observable<HttpResponse<TaskGroup[]>> {
    const options = createRequestOption(req);
    return this.http.get<TaskGroup[]>(this.resourceUrl + 'task-groups', { params: options, observe: 'response' })
        .map((res: HttpResponse<TaskGroup[]>) => this.taskGroupService.convertArrayResponse(res));
  } */

  // get all the taskGroup falling in this date range.
  queryTaskGroupsByDate(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get(this.resourceUrl + 'task-groups', { params: options, observe: 'response' });
  }

  // get all the tasks of taskGroup or active task or all task list, falling in this date range.
  // the kind of task list depends on the source param
  queryTasks(req?: any): Observable<any> {
    // const reqObj = {'map': req};
    const options = createRequestOption(req);
    return this.http.get(this.resourceUrl + 'tasks', { params: options, observe: 'response' });
        // .map((res: HttpResponse<Task[]>) => this.taskService.convertArrayResponse(res));
  }

  // upload all the files
  // source can be task or taskGroup
  // id can be zero or any id
  // if taskGroup and id is zero, a new taskGroup will be created
  // if task and id is zero, error is thrown
  pushFilesToStorage(source: string, id: number, fileList: FileList): Observable<HttpEvent<{}>> {

    if (fileList.length <= 0) {
      return null;
    }

    const formdata: FormData = new FormData();

    // formdata.append('file', fileList);

    for (let i = 0; i < fileList.length; i++) {
      formdata.append('file[]', fileList.item(i));
    }

    const finalUrl: string = this.resourceUrl + 'upload-files/' + source + '/' + id;
    console.log(finalUrl);
    const req = new HttpRequest('POST', finalUrl, formdata, {
      reportProgress: true,
      responseType: 'text'
    });

    return this.http.request(req);
  }

  getDownloadLink(source: string, id: number, selectedTasks: Task[]): string {
    let selectedIds = '';

    if (selectedTasks != null) {
      selectedTasks.forEach((task) => {
        selectedIds += task.id + ',';
      });
    }

    const url = this.resourceUrl + 'download-files/' + source + '/' + id + '/' +
           (selectedIds == null || selectedIds === '' ? '0' : selectedIds);

    console.log(url);
    return url;
  }

  downloadFiles(source: string, id: number, selectedTasks: Task[]): Observable<any> {
    const finalUrl: string = this.getDownloadLink(source, id, selectedTasks);
    return this.http.get(finalUrl, { responseType: 'blob' });
  }

  /**
   * update given list of snFiles
   * @param snFiles
   */
  updateSnFiles(snFiles: SnFile[]): Observable<any> {
    return this.http.put(this.resourceUrl + 'update-snfiles', snFiles);
  }

  /**
   * update given list of tasks
   * tasks - list of tasks to be updated
   * historyObe - history records of are created based on status and notes from historyObe
   * fieldNames - comma separated fields of task that are updated.  Only for display purpose, the fieldNames are not modified
   */
  updateTasks(tasks: Task[], historyObe: TaskHistory, fieldNames: String)
  : Observable<any> {

    const paramObj = {
      'tasks': tasks,
      'historyObe': historyObe,
      'fieldNames': fieldNames
    };

    return this.http.put(this.resourceUrl + 'update-tasks', paramObj);
  }

  findSnFiles(taskId: number): Observable<any> {
    return this.http.get(this.resourceUrl + 'snfiles-of-task/' + taskId);
  }

  findHistory(taskId: number): Observable<any> {
    return this.http.get(this.resourceUrl + 'history-of-task/' + taskId);
  }
}
