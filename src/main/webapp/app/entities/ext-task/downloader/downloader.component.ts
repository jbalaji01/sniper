import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';

import { DomSanitizer, SafeUrl } from '@angular/platform-browser';

import { JhiEventManager, JhiParseLinks, JhiAlertService } from 'ng-jhipster';

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
      this.extTaskService.downloadFiles(this.source, this.id, this.selectedTasks)
      .subscribe(
        (data) => {
          this.jhiAlertService.success('success! downloaded files. ');
          console.log(data);
          this.saveToLocal(data);
          this.uponCompletion(true);
        },
        (err) => {
          this.jhiAlertService.error('error in download! ' + err.message, null, null);
          console.log(JSON.stringify(err));
        },
        () => this.jhiAlertService.success('downloaded files', null, null)
      );
    }

  saveToLocal(response) {
    console.log('inside saveToLocal');
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

     // true if successful completion
  uponCompletion(status: boolean) {
    this.onCompletion.emit(this.id);
  }
}
