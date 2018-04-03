import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

import { JhiAlertService } from 'ng-jhipster';

import { Task } from '../../task/task.model';
import { ExtTaskService } from '../ext-task.service';

@Component({
  selector: 'jhi-downloader',
  templateUrl: './downloader.component.html',
  styles: []
})
export class DownloaderComponent implements OnInit {
  @Input() source: string;
  @Input() id: number;
  @Input() selectedTasks: Task[];
  @Output() onCompletion = new EventEmitter<number>();

  // downloadLink: string;
  safeUrl: SafeUrl;
  public pending = false;

  constructor(
    private extTaskService: ExtTaskService,
    private jhiAlertService: JhiAlertService,
    private sanitizer: DomSanitizer) {

    }

  ngOnInit() {
    // this.downloadLink = this.getDownloadLink();
    this.safeUrl = this.getDownloadLink();
  }

    // what to display in the download button
    getBanner() {

      const banner = this.source === 'taskGroup' ? 'Download files of this taskGroup' :
          this.source === 'selectedTasks' ? ('Download ' + (this.selectedTasks == null ? 0 : this.selectedTasks.length) + ' task(s)') :
          this.source === 'exportXlsx' ? 'Export xlsx of this taskGroup' :
          '';
          // console.log('in download banner ' + this.source + ' ' + banner);
      return banner;
    }

    getDownloadLink(): SafeUrl {
      const url = this.extTaskService.getDownloadLink(this.source, this.id, this.selectedTasks);
      // const urlCreator = window.URL;
      const safeUrl: SafeUrl = this.sanitizer.bypassSecurityTrustUrl(url);
        //  urlCreator.createObjectURL(data));
      return safeUrl;
    }

    afterDownload(event) {
      this.pending = true;

      this.extTaskService.downloadFiles(this.source, this.id, this.selectedTasks)
      .subscribe(
        (data) => {
          this.jhiAlertService.success('success! downloaded files. ');
          console.log(data);

          if (this.source === 'exportXlsx') {
              this.saveXlsxToLocal(data);
          } else {
              this.saveZipToLocal(data);
          }

          this.uponCompletion(true);
          this.pending = false;
        },
        (err) => {
          this.jhiAlertService.error('error in download! ' + err.message, null, null);
          console.log(JSON.stringify(err));
          this.pending = false;
        },
        () => this.jhiAlertService.success('downloaded files', null, null)
      );
    }

  saveZipToLocal(response) {
    console.log('inside saveZipToLocal');
    const ieEDGE = navigator.userAgent.match(/Edge/g);
    const ie = navigator.userAgent.match(/.NET/g); // IE 11+
    const oldIE = navigator.userAgent.match(/MSIE/g);

    const blob = new Blob([response], { type: 'application/octet-stream'});
    const fileName = 'files.zip';

    if (ie || oldIE || ieEDGE) {
      console.log('got to ie');
      window.navigator.msSaveBlob(blob, fileName);
    } else {
      const reader = new FileReader();
      reader.onloadend = function() {
        console.log('onloadend');
        window.location.href = reader.result;
      };
      console.log('readAsDataURL');
      reader.readAsDataURL(blob);
    }
  }

  saveXlsxToLocal(response) {
    console.log('inside saveXlsxToLocal');
    const ieEDGE = navigator.userAgent.match(/Edge/g);
    const ie = navigator.userAgent.match(/.NET/g); // IE 11+
    const oldIE = navigator.userAgent.match(/MSIE/g);

    const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'});
    const fileName = 'data.xlsx';

    if (ie || oldIE || ieEDGE) {
      console.log('got to ie');
      window.navigator.msSaveBlob(blob, fileName);
    } else {
      const reader = new FileReader();
      reader.onloadend = function() {
        console.log('onloadend');
        window.location.href = reader.result;
      };
      console.log('readAsDataURL');
      reader.readAsDataURL(blob);
    }
  }

     // true if successful completion
  uponCompletion(status: boolean) {
    this.onCompletion.emit(this.id);
  }
}
