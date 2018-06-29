import { Injectable, Component } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { SnFileBlob } from './sn-file-blob.model';
import { SnFileBlobService } from './sn-file-blob.service';

@Injectable()
export class SnFileBlobPopupService {
    private ngbModalRef: NgbModalRef;

    constructor(
        private modalService: NgbModal,
        private router: Router,
        private snFileBlobService: SnFileBlobService

    ) {
        this.ngbModalRef = null;
    }

    open(component: Component, id?: number | any): Promise<NgbModalRef> {
        return new Promise<NgbModalRef>((resolve, reject) => {
            const isOpen = this.ngbModalRef !== null;
            if (isOpen) {
                resolve(this.ngbModalRef);
            }

            if (id) {
                this.snFileBlobService.find(id)
                    .subscribe((snFileBlobResponse: HttpResponse<SnFileBlob>) => {
                        const snFileBlob: SnFileBlob = snFileBlobResponse.body;
                        this.ngbModalRef = this.snFileBlobModalRef(component, snFileBlob);
                        resolve(this.ngbModalRef);
                    });
            } else {
                // setTimeout used as a workaround for getting ExpressionChangedAfterItHasBeenCheckedError
                setTimeout(() => {
                    this.ngbModalRef = this.snFileBlobModalRef(component, new SnFileBlob());
                    resolve(this.ngbModalRef);
                }, 0);
            }
        });
    }

    snFileBlobModalRef(component: Component, snFileBlob: SnFileBlob): NgbModalRef {
        const modalRef = this.modalService.open(component, { size: 'lg', backdrop: 'static'});
        modalRef.componentInstance.snFileBlob = snFileBlob;
        modalRef.result.then((result) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        }, (reason) => {
            this.router.navigate([{ outlets: { popup: null }}], { replaceUrl: true, queryParamsHandling: 'merge' });
            this.ngbModalRef = null;
        });
        return modalRef;
    }
}
