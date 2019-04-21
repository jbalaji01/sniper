import { Component, OnInit, OnDestroy } from '@angular/core';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs/Subscription';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';
import { Principal } from '../../../shared';

import { Task } from '../../task/task.model';
import { TaskService } from '../../task/task.service';
import { SnFile, ChosenFactor } from '../../sn-file/sn-file.model';
import { ExtTaskService } from '../ext-task.service';

@Component({
  selector: 'jhi-ext-sn-file',
  templateUrl: './ext-sn-file.component.html',
  styleUrls: ['./ext-sn-file.component.scss']
})
export class ExtSnFileComponent implements OnInit, OnDestroy {

  private subscription: Subscription;

  taskId: number;
  task: Task;
  snFiles: SnFile[];

  // selectedSnFiles: SnFile[];
  // ChosenFactor = ChosenFactor;

  constructor(
    private principal: Principal,
    public activeModal: NgbActiveModal,
    private extTaskService: ExtTaskService,
    private taskService: TaskService,
    private dataUtils: JhiDataUtils,
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
    this.loadTask(this.taskId);
    // this.loadSnFiles(this.taskId);
  }

  loadTask(taskId: number) {
    this.taskService.find(taskId)
            .subscribe((taskResponse: HttpResponse<Task>) => {
                this.task = taskResponse.body;
                this.loadSnFiles(this.taskId);
            });
  }

  // get the snFiles of the task id
  loadSnFiles(taskId) {
    this.extTaskService.findSnFiles(taskId)
    .subscribe(
      (data) => {
        this.snFiles = data;
        // console.log(JSON.stringify(this.snFiles));
        this.decideFinalCountOfSnFiles();
      },
      (err) => this.jhiAlertService.error(err.detail, null, null),
      () => this.jhiAlertService.success('loaded files info', null, null)
    );
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  composeSelectedSnFiles(): SnFile[] {
    const selectedSnFiles: SnFile[] = [];

    this.snFiles.forEach((snFile) => {
      if (snFile['isSelected']) {
        selectedSnFiles.push(snFile);
      }
    });

    console.log('got ' + selectedSnFiles.length + ' files');
    return selectedSnFiles;
  }

  saveSnFiles() {
    const selectedSnFiles: SnFile[] = this.composeSelectedSnFiles();
    this.extTaskService.updateSnFiles(selectedSnFiles).subscribe(
      (data) => {
        this.jhiAlertService.success('success! ' + data.msg);
        console.log('updateSnFiles msg=' + JSON.stringify(data.msg));
        this.loadAll();
      },
      (err) => this.jhiAlertService.error('error in updating snFiles!' + err, null, null),
      () => this.jhiAlertService.success('updated snFiles', null, null)
    );
  }

  decideFinalCountOfSnFiles() {
    this.snFiles.forEach((snFile) => {
      this.decideFinalCountOfSnFile(snFile);
    });
  }

  decideFinalCountOfSnFile(snFile: SnFile) {
    snFile['finalCount'] = '';
    if (!this.principal.hasAnyAuthorityDirect(['ROLE_MANAGER']) && !this.task.hasPMSignedOff) {
      return;
    }
    // console.log('test2231 ' + snFile['finalCount'] + ' ' + snFile.chosenFactor);
    // console.log(JSON.stringify(snFile));

    // const finalCountMap = new Map<ChosenFactor, number>();
    // if (snFile.isAudio) {
    //   finalCountMap.set(ChosenFactor.TIME_FRAME, snFile.finalTimeFrame);
    // } else {
    //   finalCountMap.set(ChosenFactor.WS_LINE_COUNT, snFile.wsFinalLineCount);
    //   finalCountMap.set(ChosenFactor.WOS_LINE_COUNT, snFile.wosFinalLineCount);
    // }
/*
    if (this.extTaskService.varToEnumStringVal(ChosenFactor.TIME_FRAME, ChosenFactor) ===
        this.extTaskService.varToEnumStringVal(snFile.chosenFactor, ChosenFactor)) {
          snFile['finalCount'] = snFile.finalTimeFrame;
    }

    if (this.extTaskService.varToEnumStringVal(ChosenFactor.WS_LINE_COUNT, ChosenFactor) ===
        this.extTaskService.varToEnumStringVal(snFile.chosenFactor, ChosenFactor)) {
          snFile['finalCount'] = snFile.wsFinalLineCount;
    }

    if (this.extTaskService.varToEnumStringVal(ChosenFactor.WOS_LINE_COUNT, ChosenFactor) ===
        this.extTaskService.varToEnumStringVal(snFile.chosenFactor, ChosenFactor)) {
          snFile['finalCount'] = snFile.wosFinalLineCount;
    }
*/

    if (this.extTaskService.varToEnumStringValCF(ChosenFactor.TIME_FRAME) ===
        this.extTaskService.varToEnumStringValCF(snFile.chosenFactor)) {
          snFile['finalCount'] = snFile.finalTimeFrame;
    }

    if (this.extTaskService.varToEnumStringValCF(ChosenFactor.WS_LINE_COUNT) ===
        this.extTaskService.varToEnumStringValCF(snFile.chosenFactor)) {
          snFile['finalCount'] = snFile.wsFinalLineCount;
    }

    if (this.extTaskService.varToEnumStringValCF(ChosenFactor.WOS_LINE_COUNT) ===
        this.extTaskService.varToEnumStringValCF(snFile.chosenFactor)) {
          snFile['finalCount'] = snFile.wosFinalLineCount;
    }

    // finalCountMap.forEach((value: number, key: ChosenFactor) => {
    //   // console.log( '[' + this.extTaskService.varToEnumStringVal(key, ChosenFactor) + '] = [' + snFile.chosenFactor + ']' );

    //   if (this.extTaskService.varToEnumStringVal(key, ChosenFactor) === this.extTaskService.varToEnumStringVal(snFile.chosenFactor, ChosenFactor)) {

    //   }
    // });

    // snFile['finalCount'] = finalCountMap.has(ChosenFactor[snFile.chosenFactor]) ?
    //                         finalCountMap.get(ChosenFactor[snFile.chosenFactor]) : '';

    // if (snFile.isAudio && snFile.chosenFactor === ChosenFactor.TIME_FRAME) {
    //   // console.log('test 100 ' + snFile.finalTimeFrame);
    //   snFile['finalCount'] = snFile.finalTimeFrame;
    // }
    // // console.log('test2232 ' + snFile['finalCount'] + ' ' + snFile.chosenFactor);

    // if ((!snFile.isAudio) && (snFile.chosenFactor === ChosenFactor.WS_LINE_COUNT)) {
    //   snFile['finalCount'] = snFile.wsFinalLineCount;
    // }
    // // console.log('test2233 ' + snFile['finalCount'] + ' ' + snFile.chosenFactor);

    // if ((!snFile.isAudio) && (snFile.chosenFactor === ChosenFactor.WOS_LINE_COUNT)) {
    //   snFile['finalCount'] = snFile.wosFinalLineCount;
    // }
    console.log('test2234 [' + snFile['finalCount'] + '] [' + snFile.chosenFactor + ']');
  }

  // called when input changes in ws, wos or audio time count field
  updateChosenFactor(snFile: SnFile, chosenFactorStr: string) {
    // console.log(chosenFactor);
    snFile['isSelected'] = true;
    const cfstr: string = this.extTaskService.varToEnumStringValCF(chosenFactorStr);
    snFile.chosenFactor = ChosenFactor[cfstr];
    this.decideFinalCountOfSnFile(snFile);
    console.log('test223 ' + snFile['finalCount']);
  }

  // called when choosen drop down is selected
  updateChosenFactorDropdown(snFile: SnFile) {
    this.updateChosenFactor(snFile, snFile.chosenFactor + '');
  }

  byteSize(field) {
    if (field == null) {
      return '';
    }
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  previousState() {
    window.history.back();
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  trackId(index: number, item: SnFile) {
    return item.id;
  }

}
/*
@Component({
  selector: 'jhi-ext-snfile-info-popup',
  template: ''
})
export class ExtSnFilePopupComponent implements OnInit, OnDestroy {
  private ngbModalRef: NgbModalRef;
  routeSub: any;

  constructor(
    private modalService: NgbModal,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
      this.routeSub = this.route.params.subscribe((params) => {
          if ( params['id'] ) {
              this.open(ExtSnFileComponent as Component, params['id']);
          } else {
              this.open(ExtSnFileComponent as Component);
          }
      });
  }

  open(component: Component, id?: number | any): Promise<NgbModalRef> {
    return new Promise<NgbModalRef>((resolve, reject) => {
        const isOpen = this.ngbModalRef !== null;
        if (isOpen) {
            resolve(this.ngbModalRef);
        }

        if (id) {
            // this.userInfoService.find(id)
            //     .subscribe((userInfoResponse: HttpResponse<UserInfo>) => {
            //         const userInfo: UserInfo = userInfoResponse.body;
            //         if (userInfo.dateOfBirth) {
            //             userInfo.dateOfBirth = {
            //                 year: userInfo.dateOfBirth.getFullYear(),
            //                 month: userInfo.dateOfBirth.getMonth() + 1,
            //                 day: userInfo.dateOfBirth.getDate()
            //             };
            //         }
            //         if (userInfo.dateOfJoin) {
            //             userInfo.dateOfJoin = {
            //                 year: userInfo.dateOfJoin.getFullYear(),
            //                 month: userInfo.dateOfJoin.getMonth() + 1,
            //                 day: userInfo.dateOfJoin.getDate()
            //             };
            //         }
            //         userInfo.lastLogin = this.datePipe
            //             .transform(userInfo.lastLogin, 'yyyy-MM-ddTHH:mm:ss');
                    // this.ngbModalRef = this.userInfoModalRef(component, userInfo);
                    this.ngbModalRef = this.userInfoModalRef(component);
                    resolve(this.ngbModalRef);
                // });
        } else {
            // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
            setTimeout(() => {
                this.ngbModalRef = this.userInfoModalRef(component);
                resolve(this.ngbModalRef);
            }, 0);
        }
    });
}

userInfoModalRef(component: Component): NgbModalRef {
    const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
    // modalRef.componentInstance.userInfo = userInfo;
    modalRef.result.then((result) => {
        this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
        this.ngbModalRef = null;
    }, (reason) => {
        this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
        this.ngbModalRef = null;
    });
    return modalRef;
}

  ngOnDestroy() {
      this.routeSub.unsubscribe();
  }
} */
