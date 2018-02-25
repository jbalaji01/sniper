import { Component, OnInit, EventEmitter, Input, Output } from '@angular/core';
import { HttpClient, HttpResponse, HttpEventType } from '@angular/common/http';
import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

import { ExtTaskService } from '../ext-task.service';
import { HttpHeaderResponse } from '@angular/common/http/src/response';

@Component({
  selector: 'jhi-uploader',
  templateUrl: './uploader.component.html',
  styles: []
})
export class UploaderComponent implements OnInit {
  @Input() source: string;
  @Input() id: number;
  @Output() onCompletion = new EventEmitter<number>();

  selectedFiles: FileList;
  // currentFileUpload: File;

  progress: { percentage: number, isUploaded: boolean } = { percentage: 0, isUploaded: false };

  constructor(private extTaskService: ExtTaskService,
    private jhiAlertService: JhiAlertService) { }

  ngOnInit() {
  }

  // what to display in the upload button
  getBanner() {
    return this.source === 'taskGroup' ?
        this.id === 0 ? 'Create new tasks' : 'Add more tasks'
        : '';
  }

  selectFile(event) {
    console.log('source = ' + this.source + ' id = ' + this.id);
    this.selectedFiles = event.target.files;
    this.upload();
  }

  upload() {
    this.progress.percentage = 0;
    this.progress.isUploaded = true;

    // console.log('ets ' + this.source);
    this.extTaskService.pushFilesToStorage(this.source,
                    this.id, this.selectedFiles).subscribe((event) => {
                      switch (event.type) {
                        case HttpEventType.Sent:
                          // return `Uploading file "${file.name}" of size ${file.size}.`;
                          return;

                        case HttpEventType.UploadProgress:
                          // Compute and show the % done:
                          this.progress.percentage = Math.round(100 * event.loaded / event.total);
                          // const percentDone = Math.round(100 * event.loaded / event.total);
                          // return `File "${file.name}" is ${percentDone}% uploaded.`;
                          return;

                        case HttpEventType.Response:
                          this.checkResponse(event);
                          // return `File "${file.name}" was completely uploaded!`;
                          return;

                        case HttpEventType.ResponseHeader:
                          this.checkResponseHeader(event);
                          return;

                        default: // type 3 is download,  not sure why download type is received
                          // console.log( `File surprising upload event: ${event.type}.`);
                          // this.jhiAlertService.error(`File surprising upload event: ${event.type}.`);
                      }
    });
  }

  checkResponseHeader(responseHeader: HttpHeaderResponse) {
    // console.log('crh ' + responseHeader.statusText );
    if (!responseHeader.ok) {
      this.jhiAlertService.error(`upload failed : ${responseHeader.status} ${responseHeader.statusText}.`);
    }

    this.uponCompletion(responseHeader.ok);
  }

  checkResponse(response: HttpResponse<any>) {
    // console.log('crh ' + response.statusText );
    if (!response.ok) {
      this.jhiAlertService.error(`upload failed : ${response.status} ${response.statusText}.`);
    }

    this.uponCompletion(response.ok);
  }

  /*
  upload() {
    this.progress.percentage = 0;
    this.progress.isUploaded = true;

    console.log('ets ' + this.source);
    this.extTaskService.pushFilesToStorage(this.source,
                    this.id, this.selectedFiles).subscribe((event) => {
      console.log('up ' + event.type);
      if (event.type === HttpEventType.UploadProgress) {
        this.progress.percentage = Math.round(100 * event.loaded / event.total);
      } else if (event instanceof HttpResponse) {
        console.log('File upload completed!');
        this.uponCompletion(event);
      }
    });
  }
  */

  // true if successful completion
  uponCompletion(status: boolean) {

    this.onCompletion.emit(this.id);
    this.progress.isUploaded = false;
  }

}
