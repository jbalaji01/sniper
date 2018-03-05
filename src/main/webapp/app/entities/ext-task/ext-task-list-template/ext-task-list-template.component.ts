import { Component, OnInit, EventEmitter, Input, Output, ViewChild, SkipSelf } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { DatePipe, LowerCasePipe } from '@angular/common';
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
import { SOURCE } from '@angular/core/src/di/injector';
import {PatternValidator} from '@angular/forms';
import { isNull } from 'util';
import { isDefined } from '@ng-bootstrap/ng-bootstrap/util/util';

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
    selectedTasks: Task[] = [];
    masterMenuArr: Array<object> = [
      { 'head': MASTER_MENU_HEADER.SOURCE, 'list' : [BUNDLE_FIELD.COMPANY, BUNDLE_FIELD.HOSPITAL, BUNDLE_FIELD.DOCTOR]},
      { 'head': MASTER_MENU_HEADER.PLAYER, 'list' : [ BUNDLE_FIELD.OWNER, BUNDLE_FIELD.TRANSCRIPT, BUNDLE_FIELD.EDITOR, BUNDLE_FIELD.MANAGER]},
      { 'head': MASTER_MENU_HEADER.STATUS, 'list' : ['Status']},
    ];
    masterSelectedItem: any;
    childSelectedItem: any;
    childMenuList: any[]= [];
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
  // To load child menu accordingly
  loadChildMenu() {
    let masterHead: MASTER_MENU_HEADER;
    console.log(this.masterSelectedItem);
    this.childMenuList = [];
    masterHead = this.findMasterSelectedItemType();
   // console.log('lcm ' + masterHead);
    this.loadChildItems(masterHead);
    // if(masterHead==MASTER_MENU_HEADER.SOURCE && this.masterSelectedItem==BUNDLE_FIELD)
   /* for (let i = 1 ; i < 11 ; i++)  {
    this. childMenuList. push (this.masterSelectedItem + ' ' + i);
    }*/
    }
    // To find the heading of master selected item
    findMasterSelectedItemType(): MASTER_MENU_HEADER {

      let requiredType: MASTER_MENU_HEADER = null;

      this.masterMenuArr.forEach((masterItemType ) => {

        const fieldMap = new Map<string, string>();
        masterItemType['list'].forEach ( (masterType) => {
          const localMasterType = (BUNDLE_FIELD)[masterType];
          const localSelectedItem = (BUNDLE_FIELD)[this.masterSelectedItem];
         fieldMap.set(masterType, '');
          // console.log('test 12 ' + localMasterType + ' selected= ' + localSelectedItem);
          // if((localSelectedItem + '') === (localMasterType + '')) {
            //  if(BUNDLE_FIELD[masterType]===BUNDLE_FIELD[this.masterSelectedItem]) {
            //   return masterItemType['head'];
          // }
        });

          if (fieldMap.has(this.masterSelectedItem)) {
            // console.log("hurray!!! " + masterItemType['head']);
            requiredType = masterItemType['head'];
          }

        });
        // console.log('sorry no type');
        return requiredType;
          }
   /*  findMasterSelectedItemType(): MASTER_MENU_HEADER {
    this.masterMenuArr.forEach((masterItemType ) => {
      masterItemType['list'].forEach( masterType => {
        const localMasterType = (BUNDLE_FIELD)[masterType];
        const localSelectedItem = (BUNDLE_FIELD)[this.masterSelectedItem];
       console.log('test 12 ' + localMasterType + ' selected= ' + localSelectedItem);
        // if((localSelectedItem + '') === (localMasterType + '')) {
           if(BUNDLE_FIELD[masterType]===BUNDLE_FIELD[this.masterSelectedItem]) {
            return masterItemType['head'];
        }
      })
      });
      return null;
        } */
       /* var typeArray = Object.keys(types).map(function(type) {
          return types[type];*/
    loadChildItems(masterHead) {
      this.childMenuList = [];
      // console.log('I am' + masterHead);
      // let statuses: TaskStatus = TaskStatus;
      // let statusArray=Object.keys(statuses).map(function(type) {
      //   return TaskStatus[type];
      // });

    //   for (var enumMember in TaskStatus) {
    //     var isValueProperty = parseInt(enumMember, 10) >= 0
    //     if (isValueProperty) {
    //        console.log("enum member: ", myEnum[enumMember]);
    //     }
    //  }

      const statusArray = [];
      Object.keys(TaskStatus).forEach((status) => {
        // use  regex to check for digit.  skip if digit.  it is easy one
        const pattern = /^[0-9]*$/;
        if (!(status.match(pattern))) {
          statusArray.push(status);
          }
        });
      // console.log('all status = ' + JSON.stringify(statusArray));
     const menuMap = new Map<string, any>();
      menuMap.set(MASTER_MENU_HEADER.SOURCE, this.bundleMap[this.masterSelectedItem]);
      menuMap.set(MASTER_MENU_HEADER.PLAYER, this.bundleMap[BUNDLE_FIELD.USER]);
      menuMap.set(MASTER_MENU_HEADER.STATUS, statusArray);

      this.childMenuList = menuMap.get(masterHead);

// if(masterHead===MASTER_MENU_HEADER.SOURCE) {
//   this.childMenuList.concat(this.bundleMap[this.masterSelectedItem]);
// }
// if(masterHead===MASTER_MENU_HEADER.PLAYER) {
//   this.childMenuList.concat(this.bundleMap[BUNDLE_FIELD.USER]);
// }
// console.log('done');
// console.log(JSON.stringify(this.childMenuList));
    }

    // return display string as presented  in the menu
    displayName(child) {
      // console.log('in dn ' + JSON.stringify(child));
     let  field: string;
     field = this.masterSelectedItem.toLowerCase();
      const fieldName = field + 'Name';
     // console.log('fieldName = ' + fieldName);
    if (child.hasOwnProperty(fieldName)) {
      return child[fieldName]; 
    }
     if (child.hasOwnProperty('empCode')) {
      return child['empCode'];
    }
    return child;
    }
    onChildMenuSelection() {
    // console.log(this.childSelectedItem);
    let selectionType: MASTER_MENU_HEADER = null;
    selectionType = this.findMasterSelectedItemType();
    const functionName = 'set_' + selectionType.toLowerCase() + '_from_menu';
    console.log('calling ' + functionName);
    this[functionName]();
    const historyObe: TaskHistory = this.composeHistoryObe(TaskStatus.SETTING, 'updated ' + this.masterSelectedItem + ' with ' + this.childSelectedItem);
    this.updateTasks(this.selectedTasks, historyObe, this.masterSelectedItem);
    }
  set_source_from_menu() {
    // console.log('in source ' + 'assigning ' + this.masterSelectedItem + ' with ' 
    //          + JSON.stringify(this.childSelectedItem));
    this.selectedTasks.forEach( (task) => {
  this.setField(task , this.masterSelectedItem , this.childSelectedItem);
});   
} 
  set_player_from_menu() {
     // console.log('in player ' + 'assigning ' + this.masterSelectedItem + ' with ' + this.childSelectedItem);
      this.selectedTasks.forEach( (task) => {
          this.setField(task , this.masterSelectedItem , this.childSelectedItem);

          if (isNull(task.transcript) && isDefined(task.owner)) {
            task.transcript = task.owner ;
          }
        if (isNull(task.owner) && isDefined(task.transcript)) {
          task.owner = task.transcript ;
        }
        if (isDefined(task.owner) && isDefined(task.editor) && isDefined(task.transcript) && isDefined(task.manager)) {
          this.setStatus(task , TaskStatus.ASSIGNED);
        }
        });
    }
  set_status_from_menu() {
      // console.log('in status ' + 'assigning ' + this.masterSelectedItem + ' with ' + this.childSelectedItem);
      this.selectedTasks.forEach( (task) => {
          // this.setField(task , this.masterSelectedItem , this.childSelectedItem);
          this.setStatus(task, this.childSelectedItem);
        });
     }

    setField(task, field, value) {
      const rightField = field.toLowerCase();
      task[rightField] = value;
     // console.log('task[' + rightField + '] = ' + task[rightField]);
     // console.log(JSON.stringify(task));
    }

    // set the status into task
    // also set task's active flag based on status
    setStatus(task: Task, taskStatus) {
      task.taskStatus = taskStatus;

      const activeStatusMap = new Map<TaskStatus, string>();
      activeStatusMap.set(TaskStatus.ASSIGNED, '');
      activeStatusMap.set(TaskStatus.TRANSFERRED, '');

      const deactiveStatusMap = new Map<TaskStatus, string>();
      deactiveStatusMap.set(TaskStatus.COMPLETED, '');
      deactiveStatusMap.set(TaskStatus.CREATED, '');
      deactiveStatusMap.set(TaskStatus.MERGED, '');
      deactiveStatusMap.set(TaskStatus.DELETED, '');

      if (activeStatusMap.has(taskStatus)) {
        task.isActive = true;
      }

      if (deactiveStatusMap.has(taskStatus)) {
        task.isActive = false;
      }
    }

// To collect selected tasks
  collectSelectedTasks(selectedTask: Task) {
    selectedTask['isSelected'] = !(selectedTask && selectedTask['isSelected']);
    this.selectedTasks.length = 0;
    this.tasks.forEach((task) => {
    console.log('test 333 id= ' + task.id + ' flag= ' + task['isSelected']);
    if (task ['isSelected']) {
      this.selectedTasks.push (task);
  // console.log(task.id);
    }});
    let str = '';
    for (const selected of this.selectedTasks) {
      str += selected.id + ', ';
    }
    console.log(str);
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

    this.extTaskService.queryTasks(urlParamObj).subscribe(
      (res: HttpResponse<Task[]>) => this.onSuccess(res.body, res.headers),
      (res: HttpErrorResponse) => this.onError(res.message)
    );
  }

  private onSuccess(data, headers) {
    this.links = this.parseLinks.parse(headers.get('link'));
    this.totalItems = headers.get('X-Total-Count');
    this.queryCount = this.totalItems;
    // this.page = pagingParams.page;
    this.tasks = data;
  }

  private onError(error) {
    this.jhiAlertService.error(error.message, null, null);
  }

  /*
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
  */

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
    console.log('refresh function is called');
    const urlParamObj = this.composeUrlParam();
    this.loadTasks(urlParamObj);
  }
clone() {

}
merge() {

}
uponDoneMenuClick() {
  this.selectedTasks.forEach( (task) => {
    if (isDefined(task.transcript)) {
      task.owner = task.editor;
      this.setStatus(task , TaskStatus.ASSIGNED);
    }
    if (isDefined(task.editor)) {
      task.owner = task.manager;
      this.setStatus(task , TaskStatus.ASSIGNED);
    }
    if (isDefined(task.manager)) {
      this.setStatus(task , TaskStatus.COMPLETED);
    }
  });
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
    // this.updateSelectedTasks();
  }

  // call this fn when task checkbox is ticked or unticked
  updateSelectedTasks_dummy() {
    if (this.selectedTasks == null) {
      this.selectedTasks = [];
    }

    this.selectedTasks.length = 0;
    this.selectedTasks.push(this.tasks[0]);
    this.selectedTasks.push(this.tasks[2]);

    // console.log('setting selectedTask with ' + this.selectedTasks.length);
  }

  // updateSnFiles(snFiles: SnFile[]) {
  //   this.extTaskService.updateSnFiles(snFiles).subscribe(
  //     (data) => {
  //       this.jhiAlertService.success('success! ' + data.msg);
  //       console.log('updateSnFiles msg=' + JSON.stringify(data.msg));
  //     },
  //     (err) => this.jhiAlertService.error('error in updating snFiles!' + err, null, null),
  //     () => this.jhiAlertService.success('updated snFiles', null, null)
  //   );
  // }

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

export enum MASTER_MENU_HEADER {
  SOURCE = 'Source',
  PLAYER = 'Player',
  STATUS = 'Status'
}
