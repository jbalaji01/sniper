import { Component, OnInit, OnDestroy } from '@angular/core';

import { NgbActiveModal, NgbModalRef, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs/Subscription';
import { ActivatedRoute, Router } from '@angular/router';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { SnFile } from '../../sn-file/sn-file.model';
import { ExtTaskService } from '../ext-task.service';

@Component({
  selector: 'jhi-ext-sn-file',
  templateUrl: './ext-sn-file.component.html',
  styles: []
})
export class ExtSnFileComponent implements OnInit, OnDestroy {

  private subscription: Subscription;

  taskId: number;
  snFiles: SnFile[];

  selectedSnFiles: SnFile[];

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
    this.loadSnFiles(this.taskId);
  }

  // get the snFiles of the task id
  loadSnFiles(taskId) {
    this.extTaskService.findSnFiles(taskId)
    .subscribe(
      (data) => {
        this.snFiles = data;
      },
      (err) => this.jhiAlertService.error(err.detail, null, null),
      () => this.jhiAlertService.success('loaded files info', null, null)
    );
  }

  clear() {
    this.activeModal.dismiss('cancel');
  }

  saveSnFiles() {
    this.extTaskService.updateSnFiles(this.selectedSnFiles).subscribe(
      (data) => {
        this.jhiAlertService.success('success! ' + data.msg);
        console.log('updateSnFiles msg=' + JSON.stringify(data.msg));
      },
      (err) => this.jhiAlertService.error('error in updating snFiles!' + err, null, null),
      () => this.jhiAlertService.success('updated snFiles', null, null)
    );
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
}
